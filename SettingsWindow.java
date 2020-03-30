package ru.gb.stream200302_lesson_7;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//20а. Создаем окно настроек
public class SettingsWindow extends JFrame {

    private static final int WINDOW_WIDTH = 350; // 21а. ширина окна настроек игры
    private static final int WINDOW_HEIGHT = 270; // 21б. высота окна
    private static final int MIN_WIN_LENGTH = 3;  // 21в. минимальная выиграшная длина
    private static final int MIN_FIELD_SIZE = 3;  // 21г. минимальный размер поля
    private static final int MAX_FIELD_SIZE = 10;  // 21д. максимальный размер поля
    private static final String FIELD_SIZE_PREFIX = "Field size is: "; // 21е. выбранный размер поля =
    private static final String WIN_LENGTH_PREFIX = "Win length is: "; // 21и. выиграшная длина =

    private GameWindow gameWindow; // 22а. обратная ссылка на ГВ
    private JRadioButton humVSAI;  // 22б. окно с галочкой, точкой - чек бокс / человек-комп
    private JRadioButton humVShum;  // 22в. окно с галочкой, точкой - чек бокс / человек-человек
    private JSlider slideWinLen;  // 22г. ползунок с выбором позиции выиграшной длины
    private JSlider slideFieldSize;  // 22д. ползунок с выбором позиции размера поля

    SettingsWindow(GameWindow gameWindow) { // 23а. Конструктор. Принимаем ссылку на ГВ и присваеваем её
                                            //23а. внутри ГВ мы не можем вызвать сетингсвиндов, т.к. конструктор не совападает, а дефолтного нет
        this.gameWindow = gameWindow; // это значит, что этому объекту создаваему объекту SettingsWindow, (т.е. это уже объект не GameWindow, а СетингВин)
                                      // и этому объекту в поле GameWindow (зис это всегда текущий объект класса)
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // задаем размер окна настроек надо полодить переменну gameWindow
        Rectangle gameWindowBounds = gameWindow.getBounds();
        int posX = (int) gameWindowBounds.getCenterX() - WINDOW_WIDTH / 2; // задаем положение окна относительно центра и бОльшего окна (рисунок)
        int posY = (int) gameWindowBounds.getCenterY() - WINDOW_HEIGHT / 2;
        setLocation(posX, posY);
        setResizable(false); // 24.
        setTitle("Creating new game"); // 24.
        setLayout(new GridLayout(10, 1)); // 24.
        addGameControlsMode(); // 25б. добавляем метод выбора режима игры
        addGameControlsField(); // 29б. геймконтроль на поле
        JButton btnStart = new JButton("Start new game"); // 34б. создаем кнопку старт нью гейм
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStartClick(); // 34г. обработчки вызывает метод баттон старт клик
            }
        });
        add(btnStart); // 34в. добавляем кнопку старт нью гейм
    }
    /* 25а. Добавляем компонентны. Чтобы сильно громозко не было внутри конструктора
    * т.к. все остальное будет внутри консруктора, пишим отдельным метод*/
    private void addGameControlsMode() {
        add(new JLabel("Choose game mode")); // 26. Создаем надпись, просто текст
        //humVSAI = new JRadioButton("Human vs. AI"); // 27а. Jрадиобатон с подписью чел vs комп
        humVSAI = new JRadioButton("Human vs. AI", true); // 28д. не очевидный путь
        humVShum = new JRadioButton("Human vs. Human");   // 27б. Jрадиобатон с подписью чел vs чел
        ButtonGroup gameMode = new ButtonGroup(); // 28а. Создания режима, чтобы нельзя было выбрать оба режима
        gameMode.add(humVSAI); // 28б. либо первый
        gameMode.add(humVShum); // 28в. либо второй
        // humVSAI.setSelected(true); // 28г. Чтобы при старте окна был выбрат какой-то вариант, очевидный путь
        add(humVSAI); // 27в. добавляем
        add(humVShum); // 27г. добавляем
    }

    private void addGameControlsField() { // 29а. Создаем два лейбала, два слайбера
        JLabel lbFieldSize = new JLabel(FIELD_SIZE_PREFIX + MIN_FIELD_SIZE); //30а.в лейблах - филсайз с текущим минимальным филдсайзом
        JLabel lbWinLength = new JLabel(WIN_LENGTH_PREFIX + MIN_WIN_LENGTH); //30а. и винлайн с минимальным винлайном
        slideFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE); //31а. (min,max,текущиее значение) Конструкторы слайдеров принимают минимальное, максимальное, текущее значения
        slideWinLen = new JSlider(MIN_WIN_LENGTH, MIN_FIELD_SIZE, MIN_FIELD_SIZE); //31б. Винлайн слайдер принимают минимальное значение и минимальное значение поля, которое текущее
                 //32а. оживляем слайдеры
        slideFieldSize.addChangeListener(new ChangeListener() { //32б. добавляем слушателя изменений, новый ченьчлистер
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = slideFieldSize.getValue(); //32в. при изменении положения слайдера, нужно считать его текущее значение
                lbFieldSize.setText(FIELD_SIZE_PREFIX + currentValue); //32г. нужно отобразить его на лейбле
                slideWinLen.setMaximum(currentValue); // 32д. Слайдер с выйиграшной длиной установить максилмальное значение в текущее положение нашего слайдера (поле 10, винлайн 10)
            }
        });

        slideWinLen.addChangeListener(new ChangeListener() { //33а. делайем сладейдер с выиграшной длиной, добавляем ченьчлиснер
            @Override
            public void stateChanged(ChangeEvent e) {
                lbWinLength.setText(WIN_LENGTH_PREFIX + slideWinLen.getValue()); //33б. и будет выставлять значение на лейбле
            }
        });

        add(new JLabel("Choose field size"));
        add(lbFieldSize); //30в.
        add(slideFieldSize);
        add(new JLabel("Choose win length"));
        add(lbWinLength); //30в.
        add(slideWinLen);
    }

    private void btnStartClick() { // 34а.
        int gameMode; // 35в. обяъвляем гейм мод
        if (humVSAI.isSelected()) { // 35в. если (джей радио батон) чел против компа выбран
            gameMode = FieldPanel.MODE_HVA; // гейм мод = const 1
        } else if (humVShum.isSelected()) { // 35г. если (джей радио батон) чел против чела выбран
            gameMode = FieldPanel.MODE_HVH; // гейм мод = const 0
        } else { // 35е. создаем задел на будущее, напоминалку, чтоб потом сюда вернуться, например для режима игры в троем
            throw new RuntimeException("Unknown game mode!");
        }
        int fieldSize = slideFieldSize.getValue(); // 36. закладываем возмодность делать поля не только квадратными, но и прямоугольными
        int winLen = slideWinLen.getValue();
        gameWindow.startNewGame(gameMode, fieldSize, fieldSize, winLen); // 35б. передаем установки в окно
        setVisible(false); // 35а. закрываем окно установок
    }

}
