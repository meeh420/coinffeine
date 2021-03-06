package com.coinffeine.common.exchange.impl

import com.google.bitcoin.core.Transaction
import com.google.bitcoin.crypto.TransactionSignature

import com.coinffeine.common.FiatCurrency
import com.coinffeine.common.exchange.{Deposits, Handshake}

case class DefaultHandshake[C <: FiatCurrency](
   override val exchange: DefaultExchange[C],
   override val myDeposit: Transaction,
   override val myRefund: Transaction) extends Handshake[C](exchange) {

  override val myRefundIsSigned = ???

  override def withHerSignatureOfMyRefund(signature: TransactionSignature): DefaultHandshake[C] = {
    if (!TransactionProcessor.isValidSignature(myRefund, 0, signature)) {
      throw InvalidRefundSignature(myRefund, signature)
    }
    copy(myRefund = TransactionProcessor.addSignatures(myRefund, 0 -> signature))
  }

  override def startExchange(herDeposit: exchange.Transaction): DefaultMicroPaymentChannel[C] = {
    val deposits = Deposits(myDeposit.getOutput(0), herDeposit.getOutput(0))
    DefaultMicroPaymentChannel[C](exchange, deposits)
  }
}
