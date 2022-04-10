package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FormatEachOnEach {
  // private int nGroupCnt; // число групп
  // private int nStPosCnt; // число стартовых позиций
  // private int nBoutCnt;  // общее число заездов
  private int[][] raceMatrix;

  public FormatEachOnEach(int nRacerCnt, int nKartCnt) {
    /* Матрица заездов */
    this.raceMatrix = new int[nRacerCnt][nKartCnt];

    /* Временный массив */
    int jumbledLines[] = new int[nRacerCnt]; // Регистрационные номера
    for (int r = 0; r < nRacerCnt; r++) // Номера с 0 - n
      jumbledLines[r] = r;
    // Перемешиваем номера
    jumbledLines = startNumRandom(jumbledLines);

    // Двигаем стартовые
    int biasArr[][] = new int[nRacerCnt][nRacerCnt];
    for (int i = 0; i < nRacerCnt; i++) {
      jumbledLines = moveForward(jumbledLines);
      for (int s = 0; s < nRacerCnt; s++) {
        biasArr[i][s] = jumbledLines[s];
      }
    }

    for (int i = 0; i < nKartCnt; i++) {
      for (int s = 0; s < nRacerCnt; s++) {
        raceMatrix[s][i] = biasArr[jumbledLines[i]][s];
      }
    }

    for (int i = 0; i < raceMatrix.length; i++) {
      System.out.print("#" + (i + 1) + ":\t");
      for (int t : raceMatrix[i])
        System.out.print(t + "\t");
      System.out.println();
    }
  }

  /* Метод перемешивает массив */
  private int[] startNumRandom(int[] arr) {
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

  private int[] moveForward(int[] arr) {
    int[] temp = Arrays.copyOf(arr, arr.length);
    arr[0] = temp[temp.length - 1];
    for (int i = 0; i < temp.length - 1; i++) {
      arr[i + 1] = temp[i];
    }
    return arr;
  }
}