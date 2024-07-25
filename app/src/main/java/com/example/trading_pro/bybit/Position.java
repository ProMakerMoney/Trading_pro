package com.example.trading_pro.bybit;


public class Position {
    private int positionIdx;
    private int riskId;
    private String riskLimitValue;
    private String symbol;
    private String side;
    private String size;
    private String avgPrice;
    private String positionValue;
    private int tradeMode;
    private String positionStatus;
    private int autoAddMargin;
    private int adlRankIndicator;
    private String leverage;
    private String positionBalance;
    private String markPrice;
    private String liqPrice;
    private String bustPrice;
    private String positionMM;
    private String positionIM;
    private String tpslMode;
    private String takeProfit;
    private String stopLoss;
    private String trailingStop;
    private String unrealisedPnl;
    private String curRealisedPnl;
    private String cumRealisedPnl;
    private long seq;
    private boolean isReduceOnly;
    private String mmrSysUpdateTime;
    private String leverageSysUpdatedTime;
    private String sessionAvgPrice;
    private long createdTime;
    private long updatedTime;

    // Getters and Setters

    public int getPositionIdx() {
        return positionIdx;
    }

    public void setPositionIdx(int positionIdx) {
        this.positionIdx = positionIdx;
    }

    public int getRiskId() {
        return riskId;
    }

    public void setRiskId(int riskId) {
        this.riskId = riskId;
    }

    public String getRiskLimitValue() {
        return riskLimitValue;
    }

    public void setRiskLimitValue(String riskLimitValue) {
        this.riskLimitValue = riskLimitValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getPositionValue() {
        return positionValue;
    }

    public void setPositionValue(String positionValue) {
        this.positionValue = positionValue;
    }

    public int getTradeMode() {
        return tradeMode;
    }

    public void setTradeMode(int tradeMode) {
        this.tradeMode = tradeMode;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

    public int getAutoAddMargin() {
        return autoAddMargin;
    }

    public void setAutoAddMargin(int autoAddMargin) {
        this.autoAddMargin = autoAddMargin;
    }

    public int getAdlRankIndicator() {
        return adlRankIndicator;
    }

    public void setAdlRankIndicator(int adlRankIndicator) {
        this.adlRankIndicator = adlRankIndicator;
    }

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public String getPositionBalance() {
        return positionBalance;
    }

    public void setPositionBalance(String positionBalance) {
        this.positionBalance = positionBalance;
    }

    public String getMarkPrice() {
        return markPrice;
    }

    public void setMarkPrice(String markPrice) {
        this.markPrice = markPrice;
    }

    public String getLiqPrice() {
        return liqPrice;
    }

    public void setLiqPrice(String liqPrice) {
        this.liqPrice = liqPrice;
    }

    public String getBustPrice() {
        return bustPrice;
    }

    public void setBustPrice(String bustPrice) {
        this.bustPrice = bustPrice;
    }

    public String getPositionMM() {
        return positionMM;
    }

    public void setPositionMM(String positionMM) {
        this.positionMM = positionMM;
    }

    public String getPositionIM() {
        return positionIM;
    }

    public void setPositionIM(String positionIM) {
        this.positionIM = positionIM;
    }

    public String getTpslMode() {
        return tpslMode;
    }

    public void setTpslMode(String tpslMode) {
        this.tpslMode = tpslMode;
    }

    public String getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(String takeProfit) {
        this.takeProfit = takeProfit;
    }

    public String getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(String stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getTrailingStop() {
        return trailingStop;
    }

    public void setTrailingStop(String trailingStop) {
        this.trailingStop = trailingStop;
    }

    public String getUnrealisedPnl() {
        return unrealisedPnl;
    }

    public void setUnrealisedPnl(String unrealisedPnl) {
        this.unrealisedPnl = unrealisedPnl;
    }

    public String getCurRealisedPnl() {
        return curRealisedPnl;
    }

    public void setCurRealisedPnl(String curRealisedPnl) {
        this.curRealisedPnl = curRealisedPnl;
    }

    public String getCumRealisedPnl() {
        return cumRealisedPnl;
    }

    public void setCumRealisedPnl(String cumRealisedPnl) {
        this.cumRealisedPnl = cumRealisedPnl;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public boolean isReduceOnly() {
        return isReduceOnly;
    }

    public void setReduceOnly(boolean reduceOnly) {
        isReduceOnly = reduceOnly;
    }

    public String getMmrSysUpdateTime() {
        return mmrSysUpdateTime;
    }

    public void setMmrSysUpdateTime(String mmrSysUpdateTime) {
        this.mmrSysUpdateTime = mmrSysUpdateTime;
    }

    public String getLeverageSysUpdatedTime() {
        return leverageSysUpdatedTime;
    }

    public void setLeverageSysUpdatedTime(String leverageSysUpdatedTime) {
        this.leverageSysUpdatedTime = leverageSysUpdatedTime;
    }

    public String getSessionAvgPrice() {
        return sessionAvgPrice;
    }

    public void setSessionAvgPrice(String sessionAvgPrice) {
        this.sessionAvgPrice = sessionAvgPrice;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
