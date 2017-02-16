import java.awt.GridBagConstraints;

class GBC extends GridBagConstraints
{
  GBC(int gridx, int gridy, int gridwidth, int gridhegth, int weightx, int weighty)
  {
    this.weightx = weightx;
    this.weighty = weighty;
    this.gridx = gridx;
    this.gridy = gridy;
    this.gridwidth = gridwidth;
    this.gridheight = gridhegth;
    this.fill = 1;
  }
}

