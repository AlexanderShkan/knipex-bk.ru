package ru.gb.stream200302_lesson_7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {

    private static final int WIN_WIDTH = 507; // 3а. длина основного окна
    private static final int WIN_HEIGHT = 555; // 3а. высота окна
    private static final int WIN_POSX = 650; // 4а. локация координаты по х
    private static final int WIN_POSY = 250; // 4а. координаты по у
    private FieldPanel fieldPanel; // 17б.
    private SettingsWindow settingsWindow; // 20б.


    GameWindow() { //2.создаем конструктор,

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // 5. Устнавноить операцию закрытия окна по умолчанию (сеттер)
                                     // 5а. В скобках задаем константу (есть в описании ctrl+click)

        setSize(WIN_WIDTH, WIN_HEIGHT); // 3б. Задаем нашему окошку размер
        setLocation(WIN_POSX, WIN_POSY); // 4б. задаем локацию окна (х,у)
        setTitle("Tic Tac Toe"); // 5б. Название окна в шапке
        setResizable(false);  // 5в. запрещаем менять размер окна

        JButton btnStart = new JButton("Start new game"); // 6а. JбатОн отвечает за кнопки, создаем объекты кнопок
                                         // в () можно указать иконку и/или текс
        JButton btnStop = new JButton("<html><body><b>Exit</b></body></html>"); // 6б.кнопка выход
        //settingsWindow=new SettingsWindow(); // 20в. создаем новый сетингсвиндов
        settingsWindow=new SettingsWindow(this); // 23б. мы как объект ГВ создаем для себя СетингсВин и даем ссылку на себя
        btnStop.addActionListener(new ActionListener() { //18а. "оживляем" кнопку, вешаем лиснера/слашателя
                                                        //18а. создается новый экземпляр класса, с пустым конструктором

            @Override //18б. затем происходит переопределенения одного метода
            public void actionPerformed(ActionEvent e) { //18б. внутри которого есть параметр
                 System.exit(0);    // 19. по нажатию батон стоп говорим системе выйти из приложения со статусом 0
            }
        });
        // 18в. - п.18 можно заменить аналогичной записью
            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    settingsWindow.setVisible(true); // 20г. делаем видимым по нажают кнопки (при создании не видимый)
                }
            };

        btnStart.addActionListener(listener);

        JPanel pBtns = new JPanel (new GridLayout(1,2)); // 9. Jпанель это вертуальный прямоугольник, на который мы накладываем сетку
                                                                   // из одной строки и двух столбцов
        pBtns.add(btnStart);                   // в которые мы кладем наши кпопки из метода (на эту панельку)
        pBtns.add(btnStop);

        //JPanel fieldPanel = new FieldPanel(); // 13. Создаем панель из п.12 в ИгровомОкне
        fieldPanel = new FieldPanel();  // 17a. fieldPanel становится глобальным

        // add(btnStart, BorderLayout.SOUTH); // 7. кнопку не достаточно просто создать, её надо добавить и она должна где-то находиться, что-то делать
                                              // 7а. после добавления кпобка с размером на все поле, которая будет находиться в кординатах 0,0
        // add(btnStop, BorderLayout.NORTH);  // 8. Указываем местоположение кнопки "Выход" на севере нашего поля, кнопка будет растянута на всю длинну поля


        add(pBtns, BorderLayout.SOUTH); // 10. добавили кпонки на панельку на юге основного окна
        add(fieldPanel); // 14. добавляем понельку из п.13 на наш экран (по умолчанию в центр)

        setVisible(true);   // 2а. ,где говорим, что окошко должно быть видимимым
                            // - это метод "прилетел" из родителя или родителя родителя JFrame, т.е. метод появился из-за наследования
                           // 2б. появляется окошко размером 0,0 с координатами 0,0


    }

    void startNewGame (int gameMode, int fieldSizeX, int fieldSizeY, int winLength) { // 16.
        fieldPanel.startNewGame(gameMode, fieldSizeX, fieldSizeY, winLength);  //17в
    }
}