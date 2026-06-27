package com.peknight.api.codec.instances

import cats.data.Ior
import cats.syntax.option.*
import cats.{Applicative, Show}
import com.peknight.api.Result
import com.peknight.api.pagination.Pagination
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.sum.{BooleanType, NullType, ObjectType}
import com.peknight.codec.{Codec, Decoder, Encoder}
import com.peknight.error.{Error, Success}

trait ResultInstances:
  def encodeResult[F[_], S, P <: Pagination, E <: Error, A, Res <: Result[P, A]](
    successLabel: String = "success",
    dataLabel: String = "data",
    errorLabel: String = "error",
    paginationLabel: String = "pagination"
  )(mapError: Error => E)(
    using
    Applicative[F], ObjectType[S], NullType[S], BooleanType[S], Encoder[F, S, P], Encoder[F, S, E], Encoder[F, S, A]
  ): Encoder[F, S, Res] =
    Encoder.forProduct[F, S, Res, (Option[Boolean], Option[A], Option[E], Option[P])](
      (successLabel, dataLabel, errorLabel, paginationLabel)
    )(result => (result.success.some, result.data, result.error.map(mapError), result.pagination))

  def decodeResult[F[_], S, P <: Pagination, E <: Error, A, Res <: Result[P, A]](
    successLabel: String = "success",
    dataLabel: String = "data",
    errorLabel: String = "error",
    paginationLabel: String = "pagination"
  )(mapError: Error => E)(f: (E Ior A, Option[P]) => Res)(
    using
    Applicative[F], ObjectType[S], NullType[S], BooleanType[S],
    Decoder[F, Cursor[S], P],
    Decoder[F, Cursor[S], E],
    Decoder[F, Cursor[S], A],
    Show[S]
  ): Decoder[F, Cursor[S], Res] =
    Decoder.forProductMap[F, S, Res, (Option[Boolean], Option[A], Option[E], Option[P])](
      (successLabel, dataLabel, errorLabel, paginationLabel)
    ) {
      case (_, Some(data), Some(error), pagination) => f(Ior.both(error, data), pagination)
      case (_, Some(data), _, pagination) => f(Ior.right(data), pagination)
      case (_, _, Some(error), pagination) => f(Ior.left(error), pagination)
      case (_, _, _, pagination) => f(Ior.left(mapError(Success)), pagination)
    }

  def codecResult[F[_], S, P <: Pagination, E <: Error, A, Res <: Result[P, A]](
    successLabel: String = "success",
    dataLabel: String = "data",
    errorLabel: String = "error",
    paginationLabel: String = "pagination"
  )(mapError: Error => E)(f: (E Ior A, Option[P]) => Res)(
    using
    Applicative[F], ObjectType[S], NullType[S], BooleanType[S],
    Encoder[F, S, P], Decoder[F, Cursor[S], P],
    Encoder[F, S, E], Decoder[F, Cursor[S], E],
    Encoder[F, S, A], Decoder[F, Cursor[S], A],
    Show[S]
  ): Codec[F, S, Cursor[S], Res] =
    Codec[F, S, Cursor[S], Res](using
      encodeResult[F, S, P, E, A, Res](successLabel, dataLabel, errorLabel, paginationLabel)(mapError),
      decodeResult[F, S, P, E, A, Res](successLabel, dataLabel, errorLabel, paginationLabel)(mapError)(f),
    )
end ResultInstances
object ResultInstances extends ResultInstances
