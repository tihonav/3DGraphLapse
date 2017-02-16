import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TimeScrollBox extends JSlider
{
  private static final int MINTIMEPOINTER = 0;
  private static final int POINTSPERHOUR = 60;
  private static final int MAJORTRICKSPACING = 60;
  private static final int MINORTRICKSPACING = 10;
  private static final int largeChange = 30;
  private static final int mouseChange = 2;
  private int FirstHour;
  private int HoursAmount;
  private ChangeListener listener = new ChangeListener() {
    public void stateChanged(ChangeEvent e) {
      TimeScrollBox.this.changeEffected();
    }
  };

  private MouseWheelListener mouseListener = new MouseWheelListener() {
    public void mouseWheelMoved(MouseWheelEvent e) {
      if (e.getWheelRotation() < 0) {
        TimeScrollBox.this.increment(2);
      }
      else
        TimeScrollBox.this.decrement(2);
    }
  };

  private ArrayList<TimeScrollingListener> TIMESCROLLINGLISTENERS = new ArrayList();

  TimeScrollBox(int FirstHour, int HoursAmount)
  {
    super(0, 0 + (HoursAmount - 1) * 60, 0);
    super.addChangeListener(this.listener);
    super.addMouseWheelListener(this.mouseListener);
    super.setMajorTickSpacing(60);
    super.setMinorTickSpacing(10);
    super.setPaintTicks(true);

    this.FirstHour = FirstHour;
    this.HoursAmount = HoursAmount;

    Hashtable labels = new Hashtable();
    String[] hours = getHorsArray();
    for (int i = 0; i < hours.length; i++) {
      labels.put(Integer.valueOf(60 * i), new JLabel(hours[i] + ":00", 0));
    }
    super.setLabelTable(labels);
    super.setPaintLabels(true);
  }

  public double getTimePointer() {
    return  ((double)super.getValue()) / ((double)super.getMaximum());
  }
  public String getTime() {
    double time = time();

    Integer hours = Integer.valueOf((int)Math.floor(time));
    Integer minutes = Integer.valueOf((int)Math.floor(60.0D * (time - hours.intValue())));

    String Minutes = minutes.toString();
    String Hours = checkHour(hours.intValue()).toString();

    if (Minutes.length() < 2) Minutes = "0" + Minutes;
    if (Hours.length() < 2) Hours = " " + Hours;
    return Hours + ":" + Minutes;
  }

  public String[] getHorsArray()
  {
    String[] hours = new String[this.HoursAmount];
    for (int i = 0; i < this.HoursAmount; i++) {
      hours[i] = checkHour(this.FirstHour + i).toString();
    }
    return hours;
  }

  public void reset()
  {
    super.setValue(0);
  }
  public void increment() {
    int value = super.getValue();
    if (value < super.getMaximum())
      super.setValue(value + 1);
  }

  public void decrement() {
    int value = super.getValue();
    if (value > super.getMinimum())
      super.setValue(value - 1);
  }

  public void largeIncrement() {
    increment(30);
  }
  public void largeDecrement() {
    decrement(30);
  }
  private void increment(int change) {
    int value = super.getValue();
    if (value + change <= super.getMaximum()) {
      super.setValue(value + change);
    }
    else
      super.setValue(super.getMaximum());
  }

  private void decrement(int change) {
    int value = super.getValue();
    if (value - change >= super.getMinimum()) {
      super.setValue(value - change);
    }
    else
      super.setValue(super.getMinimum());
  }

  private double time()
  {
    double interval = this.HoursAmount - 1;
    return this.FirstHour + interval * getTimePointer();
  }
  private Integer checkHour(int hour) {
    if (hour >= 24) {
      return Integer.valueOf(hour - 24);
    }

    return Integer.valueOf(hour);
  }

  private void changeEffected()
  {
    for (TimeScrollingListener Listener : this.TIMESCROLLINGLISTENERS)
      Listener.changeTimeMoment(this);
                //System.out.format("getTimePointer() = %f\n", getTimePointer());
  }

  public void addTimeScrollingListener(TimeScrollingListener Listener)
  {
    this.TIMESCROLLINGLISTENERS.add(Listener);
  }
}

