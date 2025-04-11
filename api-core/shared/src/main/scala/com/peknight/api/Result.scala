package com.peknight.api

import cats.data.Ior

trait Result[Pagination, Error, A]:
  def ior: Error Ior A
  def success: Boolean = ior.isRight
  def data: Option[A] = ior.right
  def error: Option[Error] = ior.left
  def message: Option[String] = error.map(e => com.peknight.error.Error(e).message)
  def messages: List[String] = error.map(e => com.peknight.error.Error(e).messages).getOrElse(List.empty)
  def pagination: Option[Pagination] = None
end Result
