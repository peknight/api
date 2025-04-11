package com.peknight.api.syntax

import cats.ApplicativeError
import cats.syntax.functor.*
import com.peknight.api.Result
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.asError as applicativeErrorAsError
import com.peknight.error.syntax.either.asError as eitherAsError

trait ResultSyntax:
  extension [F[_], Res[A] <: Result[?, A], A](fra: F[Res[A]])
    def asError(using ApplicativeError[F, Throwable]): F[Either[Error, A]] =
      fra.applicativeErrorAsError.map(_.flatMap(_.ior.toEither.eitherAsError))
  end extension
end ResultSyntax
object ResultSyntax extends ResultSyntax
