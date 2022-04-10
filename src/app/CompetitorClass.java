package app;

import java.util.Arrays;

public class CompetitorClass {
  private int regNum;         // Регистрационный номер
  private int maxPoint;       // Максимальные очки
  private int[] heats;        // Хиты
  private int worstCount;     // Число худших заездов
  private int[] worstIndexes; // Индексы худших заездов
  private int clippedResult;  // Очки за вычетом худших(го)
  private int totalResult;    // Сумма очков
  private int lastHeatNum = 0; // Последний заезд в котором участвовал пилот
  private int code;

  public CompetitorClass(int regNum, int maxPoint, int worstCount) {
    this.regNum = regNum;
    this.maxPoint = maxPoint;
    this.worstCount = worstCount;
    if (worstCount > 0)
      this.worstIndexes = new int[this.worstCount];
  }

  public int getRegNum() {
    // System.out.println();
    // System.out.print("Регистрационный номер: № ");
    // System.out.println(regNum);
    // System.out.println("------------------------");
    return regNum;
  }

  // Хиты
  public int[] getHeats() {
    // System.out.print("Таблица заездов: ");
    // System.out.println(Arrays.toString(heats));
    return heats;
  }

  public void setHeats(int[] heats) {
    this.heats = heats;

    /* Считаем результат всех заездов */
    for (int c = 0; c < heats.length; c++) {
      if (heats[c] > 0 && heats[c] <= maxPoint) {
        this.totalResult += (maxPoint + 1) - heats[c];
      }
    }

    /* Считаем результат без худшего(их) */
    // Определяем худший результат. Возвращаем индекс
    int[] temp = Arrays.copyOf(heats, heats.length);
    for (int w = 0; w < this.worstCount; w++) {
      int idx = getIndexMax(temp); // Индекс худшего результата
      temp[idx] = -1;
      this.worstIndexes[w] = idx;
    }

    for (int c = 0; c < temp.length; c++) {
      if (temp[c] > 0 && temp[c] <= maxPoint) {
        this.clippedResult += (maxPoint + 1) - temp[c];
      }
    }
  }

  // Худшие места
  public int[] getWorstIndexes() { return worstIndexes; }

  // Вес по лучшим местам
  public double getPositionsWeight() {
    if (heats == null)
      return 0;
    else {
      // Сортируем по лучшим местам
      // Сумма += 1/(2 в степени числа"место")
      // Пример: Места (1, 3, 2, 5)
      // Сумма = 1/2с(1) + 1/2с(3) + 1/2с(2) + 1/2с(5)
      // Если место обнуляли, то пропускаем
      double tSum = 0;
      for (int h = 0; h < heats.length; h++) {
        if (heats[h] == 0)
          continue;
        tSum += 1 / Math.pow(2, heats[h]);
      }
      return tSum;
    }
  }

  // Результаты
  public int getClippedResult() { return clippedResult; }

  public int getTotalResult() { return totalResult; }

  // Возвращает количество мест
  public int getPlaceCount(int position) {
    int response = 0;
    for (int p : heats) {
      if (p == position) {
        response += 1;
      }
    }
    return response;
  }

  // Максимальные очки
  public int getMaxPoint() { return maxPoint; }

  // Последний заезд в котором участвовал пилот
  public int getLastHeatNum() { return this.lastHeatNum; }

  public void setLastHeatNum(int lastHeatNum) {
    this.lastHeatNum = lastHeatNum;
  }

  //.   Код - Сортировка по:.................
  //... 10 - не требует вторичной сортировки
  public int getCode() { return code; }

  public void setCode(int code) { this.code = code; }

  //------------------ Методы --------------------

  // здесь находим худший заезд и возвращаем индекс
  private int getIndexMax(int[] array) {
    int index = 0;
    int max = array[index];
    for (int i = 0; i < array.length; i++) {
      if (array[i] == 0) {
        index = i;
        break;
      }
      if (array[i] > max) {
        max = array[i];
        index = i;
      }
    }
    return index;
  }
}