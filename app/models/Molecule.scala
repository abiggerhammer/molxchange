package models

import uk.ac.cam.ch.wwmm.opsin.NameToInchi
import uk.ac.cam.ch.wwmm.opsin.NameToStructure
import uk.ac.cam.ch.wwmm.opsin.OpsinResult
import uk.ac.cam.ch.wwmm.opsin.OpsinResult.OPSIN_RESULT_STATUS.FAILURE
import uk.ac.cam.ch.wwmm.opsin.OpsinResult.OPSIN_RESULT_STATUS.SUCCESS

sealed trait Structure
case class Success(val mol: OpsinResult) extends Structure
case class Failure() extends Structure

object Structure {

/*
  def unapply[S <% Structure](s: S): Option[OpsinResult] = s match {
    case suc:Success => Some(suc.mol)
    case Failure => None
  }
*/
  
  implicit def or2structure(mol: OpsinResult): Structure = mol.getStatus() match {
    case SUCCESS => Success(mol)
    case FAILURE => Failure()
  }
}

object Success_ {

  def unapply(s: Success): Option[OpsinResult] = Some(s.mol)

}

object Failure_ {

  def unapply(f: Failure): Option[OpsinResult] = None

}

case class Molecule(val name: String, val inchi: String)

object Molecule {

  import Structure._

  def n2s = NameToStructure.getInstance()

  def apply(name: String): Option[Molecule] = or2structure(n2s.parseChemicalName(name)) match {
    case Success(mol) => Some(Molecule(name, NameToInchi.convertResultToInChI(mol)))
    case Failure() => None
  }

}

