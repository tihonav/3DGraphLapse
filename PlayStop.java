class PlayStop
  implements Runnable
{
  private boolean BLOCK = false;
  private boolean PAUSE;
  private TimeScrollBox TIME;

  PlayStop(TimeScrollBox TIME)
  {
    this.TIME = TIME;
  }
  public void run() {
    if (!this.BLOCK) {
      this.BLOCK = true;
      play();
      this.BLOCK = false;
    }
  }

  public void pause() {
    this.PAUSE = true;
  }

  private void play() {
    this.PAUSE = false;
    while ((this.TIME.getTimePointer() < 0.9999900000000001D) && (!this.PAUSE)) {
      this.TIME.increment();
      try {
        Thread.sleep(10L);
      }
      catch (InterruptedException localInterruptedException)
      {
      }
    }
  }
}

