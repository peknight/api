package com.peknight.api.pagination

import com.peknight.api.Result

trait PaginationResult[A] extends Result[List[A]]:
  def paginationInfo: PaginationInfo
end PaginationResult
