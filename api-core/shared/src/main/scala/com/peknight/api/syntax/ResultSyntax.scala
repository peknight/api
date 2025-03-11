package com.peknight.api.syntax

import cats.ApplicativeError
import cats.syntax.functor.*
import com.peknight.api.Result
import com.peknight.error.Error
import com.peknight.error.syntax.applicativeError.asError

trait ResultSyntax:
  extension [F[_], Res[A] <: Result[A], A](fra: F[Res[A]])
    def fResultLiftET(using ApplicativeError[F, Throwable]): F[Either[Error, A]] =
      fra.asError.map(_.flatMap(_.ior.toEither))
  end extension
end ResultSyntax
object ResultSyntax extends ResultSyntax
