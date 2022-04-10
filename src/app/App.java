package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class App {
  public static void main(String[] args) throws Exception {

    int nRacerCnt = 12; // число участников
    int nKartCnt = 6;   // число рабочих картов

    int worstCount = 1; // Число худших заездов
    int maxPoint;       // Максимальные очки

    /* Временный массив */
    int startPosition[] = new int[nRacerCnt]; // Регистрационные номера
    for (int r = 0; r < nRacerCnt; r++) // Номера с 0 - n
      startPosition[r] = r;
    // Перемешиваем номера
    startNumRandom(startPosition);

    /* Класс для распределения стартовых позиций */
    // Объект класса Старт со средних позиций.
    // В конструкторе кол-во участников и макс. кол-во картов
    FormatFromAveragePosition formatFromAveragePosition =
        new FormatFromAveragePosition(nRacerCnt, nKartCnt);

    maxPoint = formatFromAveragePosition.maxStartPosition();

    // startFromAveragePosition.setNget(0, 0, false);
    // startFromAveragePosition.setNget(1, 0, false);
    // startFromAveragePosition.setNget(2, 0, false);
    // startFromAveragePosition.setNget(3, 0, false);
    /*
        int[][] response;
        response = startFromAveragePosition.setNget(0, 0, false);
        System.out.println("baseRacePositions:");
        for (int a = 0; a < response.length; a++) {
          for (int i : response[a]) {
            System.out.print(i + "\t");
          }
          System.out.println();
        }

        response = startFromAveragePosition.setNget(1, 1, false);
        System.out.println("reverseRacePositions:");
        for (int a = 0; a < response.length; a++) {
          for (int i : response[a]) {
            System.out.print(i + "\t");
          }
          System.out.println();
        }

        response = startFromAveragePosition.setNget(2, 2, false);
        System.out.println("ffHorizontalBiasRacePositions:");
        for (int a = 0; a < response.length; a++) {
          for (int i : response[a]) {
            System.out.print(i + "\t");
          }
          System.out.println();
        }

        response = startFromAveragePosition.setNget(3, 2, false);
        System.out.println("revHorizontalBiasRacePositions:");
        for (int a = 0; a < response.length; a++) {
          for (int i : response[a]) {
            System.out.print(i + "\t");
          }
          System.out.println();
        }
    */
    /* CompetitorClass объединённый в List */
    int[][] results = {// по вертикали: Пилоты / по горизонтали: Финишка
                       {1, 2, 0, 1}, {2, 1, 5, 5}, {1, 2, 5, 1}, {2, 2, 5, 3},
                       {5, 1, 5, 3}, {6, 2, 6, 5}, {6, 3, 1, 3}, {2, 2, 1, 5},
                       {3, 1, 6, 5}, {6, 3, 1, 3}, {5, 4, 0, 2}, {6, 3, 1, 3}};

    int[] lastHeat = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    List<CompetitorClass> compList = new LinkedList<>();

    // Наполняем List для теста
    for (int i = 0; i < results.length; i++) {

      CompetitorClass competitorClass =
          new CompetitorClass(i, maxPoint, worstCount);
      competitorClass.setHeats(results[i]);
      competitorClass.setLastHeatNum(lastHeat[i]);

      compList.add(competitorClass);
    }

    /* Класс подсчёта результатов */
    CalculatePoints calculatePoints = new CalculatePoints(
        compList, formatFromAveragePosition.maxStartPosition());
    calculatePoints.getPositionsSortMap();

    /* Формат каждый на каждом */
    // FormatEachOnEach formatEachOnEach = new FormatEachOnEach(12, 5);
  }

  /* Перемешивает массив */
  private static int[] startNumRandom(int[] arr) {
    if (arr == null)
      return null;
    Random random = new Random();
    int[] newArray = new int[arr.length];
    List<Integer> indexes = new ArrayList<>(arr.length);
    int count = 0;
    while (true) {
      int var = random.nextInt(arr.length);
      if (!indexes.contains(var)) {
        indexes.add(var);
        newArray[var] = arr[count++];
      }
      if (count == arr.length) {
        break;
      }
    }

    System.out.print("Массив после перемешивания: ");
    System.out.println(Arrays.toString(newArray));
    System.out.println("Длина массива: " + (newArray.length));

    return newArray;
  }
}
