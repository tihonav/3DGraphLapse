import java.awt.GridBagConstraints;
import java.awt.Insets;

class GBC2 extends GridBagConstraints
{
  GBC2(int gridx, int gridy, int gridwidth, int gridhegth, int weightx, int weighty, int fill, int anchor)
  {
    this.weightx = weightx;
    this.weighty = weighty;
    this.gridx = gridx;
    this.gridy = gridy;
    this.gridwidth = gridwidth;
    this.gridheight = gridhegth;
    this.fill = fill;
    this.anchor = anchor;
  }
  public GBC2 setInsets(Insets insets) {
    this.insets = insets;
    return this;
  }
}

