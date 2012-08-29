package controllers

import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.iteratee.Enumeratee
import play.api.libs.iteratee.Enumerator
import play.api.mvc._

import models.Molecule
import models.Structure

import views._

import javax.xml.bind.DatatypeConverter

import com.ggasoftware.indigo._

object Application extends Controller {

  var indigo = new Indigo()
  var indigoInchi = new IndigoInchi(indigo)
  var renderer = new IndigoRenderer(indigo)
  indigo.setOption("render-output-format", "png")

  def index = Action {
    Ok(views.html.index(molForm))
  }

  def show = Action { implicit request =>
    molForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(errors)),
      name => name match {
	case None => BadRequest(views.html.index(molForm))
	case Some(mol) => {
	  indigo.synchronized {
	    val molecule = indigoInchi.loadMolecule(mol.inchi)
	    molecule.layout()
	    val buf = renderer.renderToBuffer(molecule)
	    val data = DatatypeConverter.printBase64Binary(buf) 
	    Ok(views.html.show(mol)(data))
	  }

	}
      }
    )
  }

  val molForm = Form(
    mapping(
      "name" -> nonEmptyText
    )(Molecule.apply)
     ((mol: Option[Molecule]) => mol match {
       case Some(m) => Some(m.name)
       case None => None
     })
  )

}
