package com.peknight.api

trait Result[A]:
  def data: Option[A]
end Result
