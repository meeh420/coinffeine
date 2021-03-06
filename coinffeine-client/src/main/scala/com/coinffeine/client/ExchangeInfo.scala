package com.coinffeine.client

import com.google.bitcoin.core.{ECKey, NetworkParameters}

import com.coinffeine.common.{CurrencyAmount, FiatCurrency, PeerConnection, BitcoinAmount}

/** A value class that contains all the necessary information relative to an exchange between
  * two peers
  *
  * @param id An identifier for the exchange
  * @param counterpart Connection parameters to the counterpart of the exchange
  * @param broker Connection parameters to one of the Coinffeine brokers
  * @param network Network parameters for the bitcoin network (mainnet, testnet, etc.)
  * @param userKey The user's bitcoin address
  * @param userFiatAddress The user's payment processor address
  * @param counterpartKey The counterpart's bitcoin address
  * @param counterpartFiatAddress The counterpart's payment processor address
  * @param btcExchangeAmount The amount of bitcoins to exchange
  * @param fiatExchangeAmount The amount of fiat money to exchange
  * @param steps The number of steps in which the exchange will happen
  * @param lockTime The block number which will cause the refunds transactions to be valid
  */
case class ExchangeInfo[C <: FiatCurrency](
    id: String,
    counterpart: PeerConnection,
    broker: PeerConnection,
    network: NetworkParameters,
    userKey: ECKey,
    userFiatAddress: String,
    counterpartKey: ECKey,
    counterpartFiatAddress: String,
    btcExchangeAmount: BitcoinAmount,
    fiatExchangeAmount: CurrencyAmount[C],
    steps: Int,
    lockTime: Long) {
  require(steps > 0, "Steps must be greater than zero")
  require(btcExchangeAmount.isPositive, "Exchange amount must be greater than zero")
  require(fiatExchangeAmount.isPositive)
  require(userKey.getPrivKeyBytes != null, "Credentials do not contain private key")
  val fiatStepAmount = fiatExchangeAmount / steps
  val btcStepAmount = btcExchangeAmount / steps

}
