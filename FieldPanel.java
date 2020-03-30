package ru.gb.stream200302_lesson_7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

//11. добавляем панельку с полем (игравая панель, поле на котором будем игарать)
public class FieldPanel extends JPanel {
    public static final int MODE_HVH =0; //35д.
    public static final int MODE_HVA =1; //35д.

    private static final int DOT_EMPTY = 0; //38а. т.к. режимов игры больше 2
    private static final int DOT_HUMAN = 1; //
    private static final int DOT_AI = 2; //
    private static final int DOT_PADDING = 5;

    private int stateGameOver; //39а. конеу игры, может быть три подвида состояния окончания игры
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private static final String MSG_WIN_HUMAN = "Победил игрок!"; //40а. Сообщения о победе
    private static final String MSG_WIN_AI = "Победил компьютер!";
    private static final String MSG_DRAW = "Ничья!";

    private static final Random RANDOM = new Random(); //41. рондомизатор

    private int[][] field;
    private int fieldSizeX;  //42а. размеры поля
    private int fieldSizeY;  //42б.
    private int winLength;  //42в. выиграшная длина
    private int cellWidth;  //42г. размер ячейки, чтобы каждый раз не высчитывать -
    private int cellHeight;  //42д. -будем их сохранять в классовые переменные (в поля класса)
    private boolean isGameOver;  //56a.
    private boolean initialized;  //49б.

     /*FieldPanel () { // 12. Конструктор Jпанели, который устанавливает ей
        setBackground(Color.green); // 12а. основной фон в зеленом цвете
    }*/

    FieldPanel() {
        initialized = false; //49в., когда создаем панель наше поле не инициализированно
        addMouseListener(new MouseAdapter() { // 50. Ловим клики мышки
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
    }

    private void setGameOver(int gameOverState) {
        isGameOver = true;
        stateGameOver = gameOverState;
        repaint();
    }

    private void update(MouseEvent e) { // 51. метод апдейт ставит в нужное место наши крестики и нолики
        if (!initialized) return; // 52. если игра не инициализирована то ничего не делаем
        if (isGameOver) return;
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) //53а. проверяем в валидную или пустую ли ячейку попали
            return; //53б. если ячейки не валидные или не пустые из метода мы выходим
        field[cellY][cellX] = DOT_HUMAN; // 54. если выше все ок, то ставим символ и проверяем на победу
        if (checkWin(DOT_HUMAN)) {
            setGameOver(STATE_WIN_HUMAN);
            return;
        }
        if (isMapFull()) {
            setGameOver(STATE_DRAW);
            return;
        }
        aiTurn();
        repaint();
        if (checkWin(DOT_AI)) {
            setGameOver(STATE_WIN_AI);
            return;
        }
        if (isMapFull()) {
            setGameOver(STATE_DRAW);
            return;
        }
    }


    public void startNewGame (int gameMode, int fieldSizeX, int fieldSizeY, int winLength) { // 15. Все действия игры будут здесь, для начала игры нам надо:
        // System.out.printf("game mode: %d\nfield size: %d, \nwin length: %d\n",gameMode, fieldSizeX, winLength); // 15a. режим игры, размер поля, винлайн,
        this.fieldSizeY = fieldSizeY; // 43а. заполнение полей
        this.fieldSizeX = fieldSizeX; // 43б.
        this.winLength = winLength; // 43в.
        field = new int[fieldSizeY][fieldSizeX]; //43г. Создание нового игрового поля, как двумерного массива
        isGameOver = false;
        initialized = true;

        repaint(); // 37б. метод репейн просит платнформу принудительно вызвать ПейнКомпонент
                    // окно все время слушает свою очередь сообщения
    }

    @Override  // 37а. для того чтобы рисовать на панельки есть переопределныный метод ПайнКомпонент
    protected void paintComponent(Graphics g) { // 37б. ПейнКомпонент вызывается методом фремворком свинг (когда и как решает платформа)
        super.paintComponent(g);
        render(g); // 44. создаем метод рендер с объектом графики g, т.к. отрисовка двумерных массивов называется рендеринг
        }

    private void render(Graphics g) { // 45.
        if (!initialized) return; // 49a. Предотвращаем деление на ноль, т.е. если наше поле не инициализировано, то ничего не делать
        int width = getWidth();
        int height = getHeight();
        cellWidth = width / fieldSizeX; // 46а. подсчет высоты и щирины ячейки исходя из ширины и высоты нащего филда -
        cellHeight = height / fieldSizeY; // 46б. - и размеров поля, которые мы передали
        g.setColor(Color.BLACK); // 47. цвет сетки поля
        for (int i = 0; i < fieldSizeY; i++) { // 48а. циклом отрисовываем поле вертикальные и горизонтальные
            int y = i * cellHeight; // 48в. высота у это произведение кол-во ячеек на размер ячейки
            g.drawLine(0, y, width, y); //48б. рисуем от начала по конца поля на высоте у
        }
        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth; // 49г.
            g.drawLine(x, 0, x, height);
        }
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isEmptyCell(x, y)) continue;
                if (field[y][x] == DOT_HUMAN) {
                    g.setColor(new Color(1, 1, 255));
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else if (field[y][x] == DOT_AI) {
                    g.setColor(Color.RED);
                    g.fillRect(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else {
                    throw new RuntimeException(
                            String.format("Can't recognize cell field[%d][%d]: %d", y, x, field[y][x]));
                }
            }
        }
        if (isGameOver) { // 56б. Если игра окончена
            showMessageGameOver(g); // то показываем сообщения об окончании игры, передаем объект графики
        }
    }

    private void showMessageGameOver(Graphics g) { // 57. Покажи сообщения об кончании игры
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (stateGameOver) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unexpected gameOver state: " + stateGameOver);
        }
    }

    // Ход компьютера
    private void aiTurn() {
        if(turnAIWinCell()) return;		// проверим, не выиграет-ли комп на следующем ходу
        if(turnHumanWinCell()) return;	// проверим, не выиграет-ли игрок на следующем ходу
        int x, y;
        do {							// или комп ходит в случайную клетку
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = DOT_AI;
    }
    // Проверка, может ли выиграть комп
    private boolean turnAIWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {				// поставим нолик в каждую клетку поля по очереди
                    field[i][j] = DOT_AI;
                    if (checkWin(DOT_AI)) return true;	// если мы выиграли, вернём истину, оставив нолик в выигрышной позиции
                    field[i][j] = DOT_EMPTY;			// если нет - вернём обратно пустоту в клетку и пойдём дальше
                }
            }
        }
        return false;
    }
    // Проверка, выиграет-ли игрок своим следующим ходом
    private boolean turnHumanWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = DOT_HUMAN;			// поставим крестик в каждую клетку по очереди
                    if (checkWin(DOT_HUMAN)) {			// если игрок победит
                        field[i][j] = DOT_AI;			// поставить на то место нолик
                        return true;
                    }
                    field[i][j] = DOT_EMPTY;			// в противном случае вернуть на место пустоту
                }
            }
        }
        return false;
    }
    // проверка на победу
    private  boolean checkWin(int c) {
        for (int i = 0; i < fieldSizeX; i++) {			// ползём по всему полю
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLength, c)) return true;	// проверим линию по х
                if (checkLine(i, j, 1, 1, winLength, c)) return true;	// проверим по диагонали х у
                if (checkLine(i, j, 0, 1, winLength, c)) return true;	// проверим линию по у
                if (checkLine(i, j, 1, -1, winLength, c)) return true;	// проверим по диагонали х -у
            }
        }
        return false;
    }
    // проверка линии
    private boolean checkLine(int x, int y, int vx, int vy, int len, int c) {
        final int far_x = x + (len - 1) * vx;			// посчитаем конец проверяемой линии
        final int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) return false;	// проверим не выйдет-ли проверяемая линия за пределы поля
        for (int i = 0; i < len; i++) {					// ползём по проверяемой линии
            if (field[y + i * vy][x + i * vx] != c) return false;	// проверим одинаковые-ли символы в ячейках
        }
        return true;
    }
    // ничья?
    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }
    // ячейка-то вообще правильная?
    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }
    // а пустая?
    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

}
