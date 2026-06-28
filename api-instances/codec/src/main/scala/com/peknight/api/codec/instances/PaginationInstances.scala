package com.peknight.api.codec.instances

import cats.Applicative
import com.peknight.api.pagination.{None, Pagination}
import com.peknight.codec.Codec
import com.peknight.codec.cursor.Cursor
import com.peknight.codec.sum.ObjectType

trait PaginationInstances:
  given codecPagination[F[_]: Applicative, S: ObjectType]: Codec[F, S, Cursor[S], Pagination] =
    Codec.map[F, S, Cursor[S], Pagination](_ => ObjectType[S].empty)(_ => None)
end PaginationInstances
object PaginationInstances extends PaginationInstances
