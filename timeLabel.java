import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;

class timeLabel extends JLabel
  implements TimeScrollingListener
{
  timeLabel(TimeScrollBox TIME, Component parent)
  {
    TIME.addTimeScrollingListener(this);
    changeTimeMoment(TIME);
    setPreferredSize(new Dimension(40, 10));
  }

  public void changeTimeMoment(TimeScrollBox time)
  {
    setText(time.getTime());
  }
}

