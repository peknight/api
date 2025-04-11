package com.peknight.api.codec.instances

import cats.Applicative
import com.peknight.codec.sum.{ObjectType, NullType, BooleanType}
import cats.data.Ior
import com.peknight.api.Result
import cats.syntax.option.*
import com.peknight.api.pagination.Pagination
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.{Codec, Decoder, Encoder}

trait ResultInstances:
  def codecResult[F[_], S, P <: Pagination, E, A, Res <: Result[P, E, A]](
    successLabel: String = "success",
    dataLabel: String = "data",
    errorLabel: String = "error",
    paginationLabel: String = "pagination"
  )(defaultError: => E)(f: (E Ior A, Option[P]) => Res)(
    using
    Applicative[F], ObjectType[S], NullType[S], BooleanType[S],
    Encoder[F, S, P], Decoder[F, Cursor[S], P],
    Encoder[F, S, E], Decoder[F, Cursor[S], E],
    Encoder[F, S, A], Decoder[F, Cursor[S], A]
  ): Codec[F, S, Cursor[S], Res] =
    Codec.forProductMap[F, S, Res, (Option[Boolean], Option[A], Option[E], Option[P])](
      (successLabel, dataLabel, errorLabel, paginationLabel)
    )(result => (result.success.some, result.data, result.error, result.pagination)) {
      case (_, Some(data), Some(error), pagination) => f(Ior.both(error, data), pagination)
      case (_, Some(data), _, pagination) => f(Ior.right(data), pagination)
      case (_, _, Some(error), pagination) => f(Ior.left(error), pagination)
      case (_, _, _, pagination) => f(Ior.left(defaultError), pagination)
    }
end ResultInstances
object ResultInstances extends ResultInstances
