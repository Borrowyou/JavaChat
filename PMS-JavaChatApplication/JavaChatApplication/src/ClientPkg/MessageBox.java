package ClientPkg;

import javax.swing.JOptionPane;

public class MessageBox
{

    public static void Show(String infoMessage, String titleBar)
    {
        /* By specifying a null headerMessage String, we cause the dialog to
           not have a header */
        Show(infoMessage, titleBar, null);
    }

public static void Show(String infoMessage, String titleBar, String headerMessage)
    {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}