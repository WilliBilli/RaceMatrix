package app;

// start from average positions
public class FormatFromAveragePosition {
  private int nGroupCnt; // число групп
  private int nStPosCnt; // число стартовых позиций
  private int nBoutCnt;  // общее число заездов

  public FormatFromAveragePosition(int nRacerCnt, int nKartCnt) {

    /* nKartCnt - максимальное возможное число картов в заезде */
    if (nRacerCnt <= nKartCnt)
      nGroupCnt = 1;
    else // пример: 23/12 = 2 группы. Округляем вверх
      nGroupCnt = (int)Math.ceil((double)nRacerCnt / nKartCnt);

    // Число стартовых позиций.
    nStPosCnt = (int)Math.ceil((double)nRacerCnt / nGroupCnt);
    // Число заездов, по 4 заезда на каждую группу
    nBoutCnt = nGroupCnt * 4;
  }

  // Общее количество заездов
  public final int heatCount() { return nBoutCnt; }

  // Максимальные очки. По количеству стартовых позиций
  public final int maxStartPosition() { return nStPosCnt; }

  public final int[][] setNget(int heat, int vBias, boolean reverse) {
    switch (heat) {
    case 0:
      return formatArray(vBias, reverse); // Прямой порядок
    case 1:
      return reverseArray(formatArray(vBias, reverse)); // обратный порядок
    case 2:
      return forwardBiasArray(
          formatArray(vBias, reverse)); // прямой со смещением
    case 3:
      return reverseArray(forwardBiasArray(
          formatArray(vBias, reverse))); // обратный со смещением
    }
    return null;
  }

  private final int[][] formatArray(int verticalBias,
                                    boolean zigzag) { // Смещение и Зигзаг
    int tempRacePositions[][] =
        new int[nGroupCnt][nStPosCnt]; // Временный массив
    int startNum = 0;
    int verticalBiasGroup = 0;
    boolean zigzagDirection = false;

    // обратный порядок
    if (!zigzag) {
      for (int stpos = 0; stpos < nStPosCnt;
           stpos++) { //число стартовых позиций

        for (int heat = nGroupCnt; heat > 0; heat--) { //число групп
          tempRacePositions[verticalBiasGroup][stpos] = startNum++;
          if (verticalBiasGroup < (nGroupCnt - 1))
            verticalBiasGroup++;
          else
            verticalBiasGroup = 0;
        }

        for (int b = 0; b < verticalBias; b++) {
          if (verticalBiasGroup < (nGroupCnt - 1))
            verticalBiasGroup++;
          else
            verticalBiasGroup = 0;
        }
      }
    } else {

      for (int stpos = 0; stpos < nStPosCnt;
           stpos++) { //число стартовых позиций
        for (int heat = nGroupCnt; heat > 0; heat--) { //число групп
          if (zigzagDirection) {
            tempRacePositions[(nGroupCnt - 1) - verticalBiasGroup][stpos] =
                startNum++;
          } else {
            tempRacePositions[verticalBiasGroup][stpos] = startNum++;
          }
          if (verticalBiasGroup < (nGroupCnt - 1))
            verticalBiasGroup++;
          else
            verticalBiasGroup = 0;
        }

        // if(step){
        if (zigzagDirection) {
          zigzagDirection = false;
        } else {
          zigzagDirection = true;
        }
        //}

        for (int b = 0; b < verticalBias; b++) {
          if (verticalBiasGroup < (nGroupCnt - 1))
            verticalBiasGroup++;
          else
            verticalBiasGroup = 0;
        }
      }
    }

    return tempRacePositions;
  }

  private final int[][] reverseArray(int[][] mass) { // Смещение и Зигзаг
    int tempRacePositions[][] =
        new int[nGroupCnt][nStPosCnt]; // Временный массив

    int reverse = nStPosCnt - 1;

    for (int h = 0; h < nStPosCnt; h++) {
      for (int v = 0; v < nGroupCnt; v++) {
        tempRacePositions[v][reverse] = mass[v][h];
      }
      if (reverse > 0)
        reverse--;
      else
        reverse = nStPosCnt - 1;
    }

    return tempRacePositions;
  }

  private final int[][] forwardBiasArray(int[][] mass) { // Смещение и Зигзаг
    int tempRacePositions[][] =
        new int[nGroupCnt][nStPosCnt]; // Временный массив

    int hBias = (int)Math.floor((double)nStPosCnt / 2);

    for (int h = 0; h < nStPosCnt; h++) {
      for (int v = 0; v < nGroupCnt; v++) {
        tempRacePositions[v][hBias] = mass[v][h];
      }
      if (hBias < nStPosCnt - 1)
        hBias++;
      else
        hBias = 0;
    }

    return tempRacePositions;
  }
}