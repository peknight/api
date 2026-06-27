package com.peknight.api.syntax

import cats.ApplicativeError
import cats.data.{EitherT, Ior, IorT}
import cats.syntax.functor.*
import com.peknight.api.Result
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.asError as aeAsError

trait ResultSyntax:
  extension [F[_], Res[A] <: Result[?, A], A](fra: F[Res[A]])
    def asError(using ApplicativeError[F, Throwable]): F[Either[Error, A]] =
      fra.aeAsError.map(x => x.flatMap(_.ior.toEither))
    def asET(using ApplicativeError[F, Throwable]): EitherT[F, Error, A] =
      EitherT(fra.aeAsError.map(_.flatMap(_.ior.toEither)))
    def asErrorIor(using ApplicativeError[F, Throwable]): F[Ior[Error, A]] =
      fra.aeAsError.map(_.fold(Ior.left, _.ior))
    def asIT(using ApplicativeError[F, Throwable]): IorT[F, Error, A] =
      IorT(fra.aeAsError.map(_.fold(Ior.left, _.ior)))
  end extension
end ResultSyntax
object ResultSyntax extends ResultSyntax
