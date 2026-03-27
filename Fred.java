import java.awt.Color;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Fred extends JFrame {

    JButton[] botones = new JButton[4];
    int[] patron = new int[5];
    Color[] paleta = {
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.YELLOW
    };
    Random rnd = new Random();

    public Fred() {
        setTitle("Juego de luces");
        setDefaultCloseOperation(3);
        setSize(300, 300);
        setLayout(new GridLayout(2, 2));

        for (int i = 0; i < 4; i++) {
            final int pos = i;
            botones[i] = new JButton("Boton " + (i + 1));
            botones[i].setBackground(Color.LIGHT_GRAY);
            botones[i].setOpaque(true);
            botones[i].setBorderPainted(false);
            botones[i].addActionListener(e -> encender(pos, 350));
            add(botones[i]);
        }

        generarPatron();
        reproducirPatron();
    }

    public void generarPatron() {
        for (int i = 0; i < patron.length; i++) {
            patron[i] = rnd.nextInt(4);
        }

        for (int n : patron) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public void reproducirPatron() {
        for (JButton b : botones) {
            b.setEnabled(false);
        }

        final int[] indice = {0};
        final boolean[] activo = {false};

        Timer t = new Timer(500, null);
        t.addActionListener(e -> {
            if (indice[0] >= patron.length) {
                t.stop();
                for (JButton b : botones) {
                    b.setEnabled(true);
                    b.setBackground(Color.LIGHT_GRAY);
                }
                return;
            }

            int pos = patron[indice[0]];

            if (!activo[0]) {
                botones[pos].setBackground(paleta[pos]);
                activo[0] = true;
                t.setDelay(1000);
            } else {
                botones[pos].setBackground(Color.LIGHT_GRAY);
                activo[0] = false;
                indice[0]++;
                t.setDelay(500);
            }
        });
        t.setInitialDelay(0);
        t.start();
    }

    public void encender(int pos, int tiempo) {
        botones[pos].setBackground(paleta[pos]);

        Timer off = new Timer(tiempo, e -> botones[pos].setBackground(Color.LIGHT_GRAY));
        off.setRepeats(false);
        off.start();
    }

    public static void main(String[] args) {
        Fred app = new Fred();
        app.setVisible(true);
    }
}