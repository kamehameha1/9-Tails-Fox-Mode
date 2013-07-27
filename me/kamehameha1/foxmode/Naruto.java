package me.kamehameha1.foxmode;

  public class Naruto {
  private String p;
  private double FoxModeTime = 60;
  private boolean Kyuubi = false;
  private double KyuubiTime = 0;
  private double swordAttack = 0;
  private boolean Kyuubied = false;
  
  Naruto(String player) {
    p = player;
  }
  
  double getFoxModeTime() {
    return FoxModeTime;
  }
  
  String getPlayer() {
    return p;
  }
  
  void setFoxModeTime(int FoxMode) {
    FoxModeTime = FoxMode;
  }
  
  void setKyuubi(boolean isKyuubi) {
    Kyuubi = Kyuubi;
    Kyuubied = true;
  }
  
  boolean hasKyuubied() {
    return Kyuubied;
  }
  
  boolean getKyuubi() {
    return Kyuubi;
  }
  
  double getKyuubiTime() {
    return KyuubiTime;
  }
  
  void setKyuubiTime(int time) {
    KyuubiTime = time;
  }
  
  void decreaseFoxMode() {
    FoxModeTime += -0.05;
  }
  
  void decreaseKyuubi() {
    KyuubiTime += -0.05;
  }
  
  void decreasePowerwave() {
    swordAttack += -0.05;
  }

  double getPowerwave() {
    return swordAttack;
  }
  
  void setPowerwave(double power) {
    swordAttack = power;
  }
}

