import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class Player {

    // For Jackson JSON Streaming
    @JsonProperty("KD")
    public double KD;
    @JsonProperty("SPM")
    public double SPM;
    @JsonProperty("TP25")
    public double TP25;
    @JsonProperty("TP5")
    public double TP5;
    @JsonProperty("TP")
    public double TP;
    @JsonProperty("WINS")
    public double WINS;
    @JsonProperty("GP")
    public double GP;
    @JsonProperty("DWNS")
    public double DWNS;
    @JsonProperty("CNTR")
    public double CNTR;
    @JsonProperty("REV")
    public double REV;
    @JsonProperty("GMTG")
    public String GMTG;
    @JsonProperty("PLT")
    public String PLT;
    @JsonProperty("KILL20")
    public double KILL20;
    @JsonProperty("KPG")
    public double KPG;
    @JsonProperty("TMWP")
    public double TMWP;
    @JsonProperty("AVGLF")
    public double AVGLF;
    @JsonProperty("DSTTRV")
    public double DSTTRV;
    @JsonProperty("HDSHTPCT")
    public double HDSHTPCT;
    @JsonProperty("GLGKL")
    public double GLGKL;
    @JsonProperty("DMGD")
    public double DMGD;
    @JsonProperty("DMGT")
    public double DMGT;
    @JsonProperty("LSTSTND")
    public double LSTSTND;

    public double getKD() {
        return KD;
    }

    public void setKD(double KD) {
        this.KD = KD;
    }

    public double getSPM() {
        return SPM;
    }

    public void setSPM(double SPM) {
        this.SPM = SPM;
    }

    public double getTP25() {
        return TP25;
    }

    public void setTP25(int TP25) {
        this.TP25 = TP25;
    }

    public double getTP5() {
        return TP5;
    }

    public void setTP5(int TP5) {
        this.TP5 = TP5;
    }

    public double getTP() {
        return TP;
    }

    public void setTP(double TP) {
        this.TP = TP;
    }

    public double getWINS() {
        return WINS;
    }

    public void setWINS(int WINS) {
        this.WINS = WINS;
    }

    public double getGP() {
        return GP;
    }

    public void setGP(int GP) {
        this.GP = GP;
    }

    public String getGMTG() {
        return GMTG;
    }

    public void setGMTG(String GMTG) {
        this.GMTG = GMTG;
    }

    public String getPLT() {
        return PLT;
    }

    public void setPLT(String PLT) {
        this.PLT = PLT;
    }

    public double getDWNS() {
        return DWNS;
    }

    public void setDWNS(int DWNS) {
        this.DWNS = DWNS;
    }

    public double getCNTR() {
        return CNTR;
    }

    public void setCNTR(int CNTR) {
        this.CNTR = CNTR;
    }

    public double getREV() {
        return REV;
    }

    public void setREV(int REV) {
        this.REV = REV;
    }

    public double getKILL20() {
        return KILL20;
    }

    public void setKILL20(double KILL20) {
        this.KILL20 = KILL20;
    }

    public double getKPG() {
        return KPG;
    }

    public void setKPG(double KPG) {
        this.KPG = KPG;
    }

    public double getTMWP() {
        return TMWP;
    }

    public void setTMWP(double TMWP) {
        this.TMWP = TMWP;
    }

    public double getAVGLF() {
        return AVGLF;
    }

    public void setAVGLF(double AVGLF) {
        this.AVGLF = AVGLF;
    }

    public double getDSTTRV() {
        return DSTTRV;
    }

    public void setDSTTRV(double DSTTRV) {
        this.DSTTRV = DSTTRV;
    }

    public double getHDSHTPCT() {
        return HDSHTPCT;
    }

    public void setHDSHTPCT(double HDSHTPCT) {
        this.HDSHTPCT = HDSHTPCT;
    }

    public double getGLGKL() {
        return GLGKL;
    }

    public void setGLGKL(int GLGKL) {
        this.GLGKL = GLGKL;
    }

    public double getDMGD() {
        return DMGD;
    }

    public void setDMGD(double DMGD) {
        this.DMGD = DMGD;
    }

    public double getDMGT() {
        return DMGT;
    }

    public void setDMGT(double DMGT) {
        this.DMGT = DMGT;
    }

    public double getLSTSTND() {
        return LSTSTND;
    }

    public void setLSTSTND(int LSTSTND) {
        this.LSTSTND = LSTSTND;
    }

    //TODO implement methods to return various percentages
//    public double getPercentageGamesWon(){
//
//    }
//
//    public double getPercentageTop25(){
//
//    }
//
//    public double getPercentageTop5(){
//
//    }
//
//    public double getKDPerPercentageTop25(){
//
//    }
//
//    public double getKDPerPercentageTop5(){
//
//    }
//
//    public double getSPMPerTimePlayed(){
//
//    }
//
// TODO add other fields as more API data is retrieved from last 20 games.....
//
//    public double getAverageTimeMovingLast20Percentage(){
//
//    }

}
