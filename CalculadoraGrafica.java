import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CalculadoraGrafica extends JFrame {

    JTextField campo1, campo2;
    JButton sumarBtn, restarBtn, multiBtn, divBtn;
    JLabel lblResultado;
    Font estiloTexto = new Font("Arial", Font.BOLD, 60);

    public CalculadoraGrafica() {
        setTitle("Mini Calculadora");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        campo1 = new JTextField(10);
        campo1.setFont(estiloTexto);
        campo2 = new JTextField(10);
        campo2.setFont(estiloTexto);

        sumarBtn = new JButton("+");
        restarBtn = new JButton("-");
        multiBtn = new JButton("*");
        divBtn = new JButton("/");

        JPanel panelBotones = new JPanel();
        panelBotones.add(sumarBtn);
        panelBotones.add(restarBtn);
        panelBotones.add(multiBtn);
        panelBotones.add(divBtn);

        lblResultado = new JLabel();
        lblResultado.setFont(estiloTexto);

        add(campo1);
        add(campo2);
        add(panelBotones);
        add(lblResultado);

        BiConsumer<JButton, DoubleBinaryOperator> asignarOperacion = (btn, operacion) ->
            btn.addActionListener(e -> {
                try {
                    double valor1 = Double.parseDouble(campo1.getText());
                    double valor2 = Double.parseDouble(campo2.getText());

                    if (btn == divBtn && valor2 == 0) {
                        lblResultado.setText("No valido");
                        return;
                    }

                    double resultadoCalc = operacion.applyAsDouble(valor1, valor2);
                    lblResultado.setText(String.valueOf(resultadoCalc));
                } catch (NumberFormatException err) {
                    lblResultado.setText("Entrada incorrecta");
                }
            });

        asignarOperacion.accept(sumarBtn, (x, y) -> x + y);
        asignarOperacion.accept(restarBtn, (x, y) -> x - y);
        asignarOperacion.accept(multiBtn, (x, y) -> x * y);
        asignarOperacion.accept(divBtn, (x, y) -> x / y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculadoraGrafica().setVisible(true));
    }
}