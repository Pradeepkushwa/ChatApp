
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Server extends JFrame {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare Components
    private JLabel heading = new JLabel("Pradeep message box");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // Constructor..
    public Server() {
        try {
            System.out.println("server is ready to accept connection");
            System.out.println("waiting...");
            server = new ServerSocket(7777);
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handelEvents();

            startReading();
           // startWriting();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handelEvents() {

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // throw new UnsupportedOperationException("Unimplemented method
                // 'keyReleased'");

                // System.out.println("key released "+e.getKeyCode());

                if (e.getKeyCode() == 10) {
                    // System.out.println("you have pressed enter button");

                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :" + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
            }
        });
    }

    private void createGUI() {
        // gui code.....

        this.setTitle("Bhopal Message [END]");
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for componets
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("1march.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // create layout of frame
        this.setLayout(new BorderLayout());

        // adding the components of frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void startReading() {
        // thread-read karke data rakhega

        Runnable r1 = () -> {

            System.out.println("reader started .....");
            try {
                while (!socket.isClosed()) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Nidhi terminated the chat");
                        JOptionPane.showMessageDialog(this, "Nidhi terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Client : " + msg);
                    messageArea.append("Nidhi : "+ msg+"\n");

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed");
            }

        };

        new Thread(r1).start();

    }

    public void startWriting() {
        // threa - data user lega and the send karega client tak

        Runnable r2 = () -> {

            System.out.println("writer started...");
            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is server.. going to start");
        new Server();
    }
}