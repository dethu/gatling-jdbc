package dev.code_n_roll.gatling.jdbc.check

import dev.code_n_roll.gatling.jdbc.JdbcCheck
import dev.code_n_roll.gatling.jdbc.Predef.ManyAnyResult
import io.gatling.core.check._

import scala.annotation.implicitNotFound

/**
  * Created by ronny on 15.05.17.
  */
trait JdbcCheckSupport {

  def simpleCheck = JdbcSimpleCheck

  val jdbcSingleResponse = singleResponse[Map[String, Any]]

  def singleResponse[T]: DefaultFindCheckBuilder[JdbcSingleTCheck.JdbcSingleTCheckType, T, T] = JdbcSingleTCheck.singleTResult[T]

  implicit def jdbcSingleTCheckMaterializer[T]: CheckMaterializer[JdbcSingleTCheck.JdbcSingleTCheckType, JdbcCheck[T], List[T], T] = JdbcSingleTCheck.singleTCheckMaterializer[T]

  val jdbcManyResponse = JdbcManyAnyCheck.ManyAnyResults
  implicit val jdbcManyCheckMaterializer: CheckMaterializer[JdbcManyAnyCheck.JdbcManyAnyCheckType, JdbcCheck[Map[String, Any]], ManyAnyResult, ManyAnyResult] = JdbcManyAnyCheck.ManyAnyCheckMaterializer

  @implicitNotFound("Could not find a CheckMaterializer. This check might not be valid for JDBC.")
  implicit def findCheckBuilder2JdbcCheck[A, P, X](findCheckBuilder: FindCheckBuilder[A, P, X])(implicit CheckMaterializer: CheckMaterializer[A, JdbcCheck[Map[String, Any]], ManyAnyResult, P]): JdbcCheck[Map[String, Any]] =
    findCheckBuilder.find.exists

  @implicitNotFound("Could not find a CheckMaterializer. This check might not be valid for JDBC.")
  implicit def findTypedCheckBuilder2JdbcCheck[A, P, X](findCheckBuilder: FindCheckBuilder[A, P, X])(implicit CheckMaterializer: CheckMaterializer[A, JdbcCheck[P], List[P], P]): JdbcCheck[P] =
    findCheckBuilder.find.exists

  @implicitNotFound("Could not find a CheckMaterializer. This check might not be valid for JDBC.")
  implicit def checkBuilder2JdbcCheck[A, P, X](checkBuilder: CheckBuilder[A, P, X])(implicit materializer: CheckMaterializer[A, JdbcCheck[Map[String, Any]], ManyAnyResult, P]): JdbcCheck[Map[String, Any]] =
    checkBuilder.build(materializer)

  @implicitNotFound("Could not find a CheckMaterializer. This check might not be valid for JDBC.")
  implicit def typedCheckBuilder2JdbcCheck[A, P, X](checkBuilder: CheckBuilder[A, P, X])(implicit materializer: CheckMaterializer[A, JdbcCheck[P], List[P], P]): JdbcCheck[P] =
    checkBuilder.build(materializer)

}