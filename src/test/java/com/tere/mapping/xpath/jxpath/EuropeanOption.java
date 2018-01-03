package com.tere.mapping.xpath.jxpath;

public class EuropeanOption {

	protected Long tradeId;
	protected java.math.BigDecimal notional;
	protected String externalTradeId;
	protected java.util.Date tradeDate;
	protected java.util.Date settlementDate;
	protected String instrument;
	protected String countryId;
	protected String sectorId;

	protected java.math.BigDecimal strike;
	protected String counterpartyName;
	protected String counterpartyId;

	public Long getTradeId() {
		return tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	public java.math.BigDecimal getNotional() {
		return notional;
	}

	public void setNotional(java.math.BigDecimal notional) {
		this.notional = notional;
	}

	public String getExternalTradeId() {
		return externalTradeId;
	}

	public void setExternalTradeId(String externalTradeId) {
		this.externalTradeId = externalTradeId;
	}

	public java.util.Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(java.util.Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public java.util.Date getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(java.util.Date settlementDate) {
		this.settlementDate = settlementDate;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public java.math.BigDecimal getStrike() {
		return strike;
	}

	public void setStrike(java.math.BigDecimal strike) {
		this.strike = strike;
	}

	public String getCounterpartyName() {
		return counterpartyName;
	}

	public void setCounterpartyName(String counterpartyName) {
		this.counterpartyName = counterpartyName;
	}

	public String getCounterpartyId() {
		return counterpartyId;
	}

	public void setCounterpartyId(String counterpartyId) {
		this.counterpartyId = counterpartyId;
	}

}
