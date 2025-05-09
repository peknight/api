package com.peknight.api

import cats.data.Ior
import com.peknight.error.Error

trait Result[Pagination, A]:
  def ior: Error Ior A
  def success: Boolean = ior.isRight
  def data: Option[A] = ior.right
  def error: Option[Error] = ior.left
  def message: Option[String] = error.map(_.message)
  def messages: List[String] = error.map(_.messages).getOrElse(List.empty)
  def pagination: Option[Pagination] = None
end Result
