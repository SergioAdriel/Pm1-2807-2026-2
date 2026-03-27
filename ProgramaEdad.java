import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProgramaEdad extends JFrame implements ActionListener {
    int width, height;
    JTextField campoEdad;
    JButton boton;
    JPanel contenedor;

    public ProgramaEdad() {
        width = 400;
        height = 400;
        setTitle("Ventana de prueba");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contenedor = new JPanel();
        campoEdad = new JTextField(5);
        boton = new JButton("Ingresar edad");

        boton.addActionListener(this);

        contenedor.add(campoEdad);
        contenedor.add(boton);

        add(contenedor);
    }

    public static void main(String[] args) {
        ProgramaEdad ventana = new ProgramaEdad();
        ventana.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String valor = campoEdad.getText().trim();

        if (valor.length() == 0) {
            JOptionPane.showMessageDialog(this, "Debes ingresar un valor.");
            return;
        }

        try {
            int numero = Integer.parseInt(valor);

            if (numero < 0) {
                JOptionPane.showMessageDialog(this, "Edad invalida.");
            } else if (numero >= 18) {
                JOptionPane.showMessageDialog(this, "Mayor de edad.");
            } else {
                JOptionPane.showMessageDialog(this, "Menor de edad.");
            }
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(this, "Solo se permiten numeros.");
        }
    }
}