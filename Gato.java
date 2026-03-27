import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Gato extends JFrame implements ActionListener {
    JButton casillas[] = new JButton[9];
    boolean turnoJugadorX;
    Font estilo = new Font("Arial", Font.BOLD, 50);
    JButton botonReset;
    JPanel tableroPanel, opcionesPanel;

    public Gato() {
        setSize(400, 400);
        setTitle("Tres en raya");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tableroPanel = new JPanel(new GridLayout(3, 3));
        opcionesPanel = new JPanel();

        for (int i = 0; i < casillas.length; i++) {
            casillas[i] = new JButton("");
            casillas[i].setFont(estilo);
            casillas[i].addActionListener(this);
            tableroPanel.add(casillas[i]);
        }

        botonReset = new JButton("Volver a iniciar");
        botonReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarJuego();
            }
        });

        opcionesPanel.add(botonReset);
        add(tableroPanel, BorderLayout.CENTER);
        add(opcionesPanel, BorderLayout.SOUTH);

        reiniciarJuego();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String simbolo;

        if (turnoJugadorX) {
            simbolo = "X";
            turnoJugadorX = false;
        } else {
            simbolo = "O";
            turnoJugadorX = true;
        }

        for (int i = 0; i < casillas.length; i++) {
            if (e.getSource() == casillas[i]) {
                casillas[i].setText(simbolo);
                casillas[i].setEnabled(false);
                comprobarGanador();
                break;
            }
        }
    }

    private void comprobarGanador() {
        int[][] lineas = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
        };

        for (int i = 0; i < lineas.length; i++) {
            String a = casillas[lineas[i][0]].getText();
            String b = casillas[lineas[i][1]].getText();
            String c = casillas[lineas[i][2]].getText();

            if (!a.equals("") && a.equals(b) && a.equals(c)) {
                bloquearTablero();
                JOptionPane.showMessageDialog(this, "Ganador: " + a);
                return;
            }
        }

        if (estaLleno()) {
            JOptionPane.showMessageDialog(this, "Empate.");
        }
    }

    private boolean estaLleno() {
        for (int i = 0; i < casillas.length; i++) {
            if (casillas[i].getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void bloquearTablero() {
        for (int i = 0; i < casillas.length; i++) {
            casillas[i].setEnabled(false);
        }
    }

    private void reiniciarJuego() {
        turnoJugadorX = true;
        for (int i = 0; i < casillas.length; i++) {
            casillas[i].setText("");
            casillas[i].setEnabled(true);
        }
    }

    public static void main(String[] args) {
        Gato app = new Gato();
        app.setVisible(true);
    }
}