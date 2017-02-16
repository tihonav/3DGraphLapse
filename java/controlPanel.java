import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class controlPanel extends JPanel
{
  private JavaRenderer renderer;
  private TimeScrollBox time;

  controlPanel(JavaRenderer renderer)
  {
    this.renderer = renderer;
    this.time = renderer.getSlider();
    addKeyListener(renderer);
    setLayout(new GridBagLayout());
    addButton("PLAY", new controlPanel.keyPressEmulator(90), new GBC2(0, 0, 1, 1, 0, 100, 1, 17));
    addButton("PAUSE", new controlPanel.keyPressEmulator(88), new GBC2(1, 0, 1, 1, 0, 100, 1, 17));
    addButton("STOP", new controlPanel.keyPressEmulator(67), new GBC2(2, 0, 1, 1, 0, 100, 1, 17));
    addButton("2D/3D", new controlPanel.keyPressEmulator(86), new GBC2(3, 0, 1, 1, 0, 100, 1, 17));

    addComponent(this.time, new GBC2(4, 0, 1, 1, 100, 100, 1, 13));
    addComponent(new timeLabel(this.time, this), new GBC2(5, 0, 1, 1, 0, 100, 1, 17).setInsets(new Insets(0, 15, 0, 15)));

    addButton("QUIT", new controlPanel.keyPressEmulator(27), new GBC2(6, 0, 1, 1, 0, 100, 1, 13));
  }

  private void addButton(String NAME, ActionListener listener, GBC2 gbc)
  {
    JButton button = new JButton(NAME);

    button.addActionListener(listener);
    addComponent(button, gbc);
  }

  private void addComponent(Component c, GBC2 gbc) {
    c.addKeyListener(this.renderer);
    add(c, gbc);
  }
  private void addSlider() {
    JSlider slider = new JSlider(0, 100, 0);
  }

  private class keyPressEmulator implements ActionListener {
    private int key;

    keyPressEmulator(int key) {
      this.key = key;
    }
    public void actionPerformed(ActionEvent e) {
      controlPanel.this.renderer.action(this.key);
    }
  }
}

