package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CalculatePoints {

  private static int maxStartPosition;
  private static int heatCount;
  private Map<Integer, List<CompetitorClass>> positionsSortMap =
      new HashMap<>();

  public CalculatePoints(List<CompetitorClass> compResult,
                         int maxStartPosition) {
    CalculatePoints.maxStartPosition = maxStartPosition;
    CalculatePoints.heatCount = compResult.get(0).getHeats().length;

    //.   Код - Сортировка по:.................
    //... 10 - не требует вторичной сортировки
    //... 2x - сортировка по лучшим местам (21 - больше 1 мест)
    //... 3x - сортировка с конца в начало (32 - по 2 столбцу)
    //... 40 - сортировка по последнему заезду

    // Сортируем по очкам
    List<CompetitorClass> tempResultList = new LinkedList<>();
    int indexMax = 0;
    while (compResult.size() > 0) {
      int maxValue = 0;
      indexMax = 0;
      for (int i = 0; i < compResult.size(); i++) {
        if (maxValue < compResult.get(i).getClippedResult()) {
          maxValue = compResult.get(i).getClippedResult();
          indexMax = i;
        }
      }
      tempResultList.add(compResult.get(indexMax));
      compResult.remove(indexMax);
    }

    // Сохраняем очки. По ним отсеиваем "tempResultList"
    List<Integer> clippedValues = new ArrayList<>();
    int tempValue = tempResultList.get(0).getClippedResult();
    clippedValues.add(tempValue);
    for (int index = 0; index < tempResultList.size(); index++) {
      if (tempValue != tempResultList.get(index).getClippedResult()) {
        tempValue = tempResultList.get(index).getClippedResult();
        clippedValues.add(tempValue);
      }
    }

    // В Мар записываем "key:tempList("tempResultList" по очкам)"
    for (int cv = 0; cv < clippedValues.size(); cv++) {
      List<CompetitorClass> tempList = new LinkedList<>();
      for (int ci = 0; ci < tempResultList.size(); ci++) {
        if (clippedValues.get(cv) ==
            tempResultList.get(ci).getClippedResult()) {
          tempList.add(tempResultList.get(ci));
        }
      }
      positionsSortMap.put(cv, tempList);
    }

    // Перезаписываем по весам
    for (int m = 0; m < positionsSortMap.size(); m++) {
      // Если в списке больше одного элемента
      if (positionsSortMap.get(m).size() > 1) {
        positionsSortMap.put(m, pointsSortUp(positionsSortMap.get(m)));
      } else {
        //.   Код - Сортировка по:.................
        //... 10 - не требует вторичной сортировки
        positionsSortMap.get(m).get(0).setCode(10);
      }
    }

    // Отмечаем по лучшим местам
    for (int mp = 0; mp < positionsSortMap.size(); mp++) {
      if (positionsSortMap.get(mp).size() > 1) {
        for (int row = 1; row < positionsSortMap.get(mp).size(); row++) {
          List<CompetitorClass> list = new LinkedList<>();
          list.add(positionsSortMap.get(mp).get(row - 1));
          list.add(positionsSortMap.get(mp).get(row));
          positionsSortMap.get(mp).get(row - 1).setCode(
              checkByBestPlaces(list));
        }
      }
    }

    // Собираем в List
    tempResultList.clear();
    for (int m = 0; m < positionsSortMap.size(); m++) {
      for (int l = 0; l < positionsSortMap.get(m).size(); l++) {
        tempResultList.add(positionsSortMap.get(m).get(l));
      }
    }

    // Сохраняем веса. По ним отсеиваем "tempResultList"
    List<Double> weightValues = new ArrayList<>();
    double tempWeightValue = tempResultList.get(0).getPositionsWeight();
    weightValues.add(tempWeightValue);
    for (int index = 0; index < tempResultList.size(); index++) {
      if (tempWeightValue != tempResultList.get(index).getPositionsWeight()) {
        tempWeightValue = tempResultList.get(index).getPositionsWeight();
        weightValues.add(tempWeightValue);
      }
    }
    positionsSortMap.clear();

    // В Мар записываем "key:tempList(по весам)"

    for (int cw = 0; cw < weightValues.size(); cw++) {
      List<CompetitorClass> tempList = new LinkedList<>();
      for (int ci = 0; ci < tempResultList.size(); ci++) {
        if (weightValues.get(cw) ==
            tempResultList.get(ci).getPositionsWeight()) {
          tempList.add(tempResultList.get(ci));
        }
      }
      positionsSortMap.put(cw, tempList);
    }

    // С конца в начало
    for (int m = 0; m < positionsSortMap.size(); m++) {
      int code = 0;
      if (positionsSortMap.get(m).size() > 1 &&
          positionsSortMap.get(m).get(0).getCode() != 10) {
        for (int c = 0; c < positionsSortMap.get(m).size(); c++) {
          // Сохранение кода
          code = positionsSortMap.get(m).get(c).getCode();
          positionsSortMap.get(m).get(c).setCode(0);
        }
        //
        positionsSortMap.put(m, lastSort(positionsSortMap.get(m)));
        positionsSortMap.get(m)
            .get(positionsSortMap.get(m).size() - 1)
            .setCode(code);
      }
    }

    /* Вывод в консоль */
    int standing = 1;
    for (int m = 0; m < positionsSortMap.size(); m++) {
      for (int l = 0; l < positionsSortMap.get(m).size(); l++) {
        System.out.print(standing + "\t");
        standing++;
        if (l == 0) {
          System.out.print(positionsSortMap.get(m).get(l).getClippedResult());
        }
        System.out.print("\t#: ");
        System.out.print(positionsSortMap.get(m).get(l).getRegNum());
        System.out.print("\tВес: ");
        System.out.print(positionsSortMap.get(m).get(l).getPositionsWeight());
        System.out.print("\tХиты: ");
        System.out.print(
            Arrays.toString(positionsSortMap.get(m).get(l).getHeats()));
        System.out.print("\tКод: ");
        System.out.print(positionsSortMap.get(m).get(l).getCode());
        System.out.print("\t\tLH: ");
        System.out.println(positionsSortMap.get(m).get(l).getLastHeatNum());
      }
      System.out.println();
    }
    System.out.println("--------------------------------------------");
    /* Вывод в консоль */
    //
  }

  public Map<Integer, List<CompetitorClass>> getPositionsSortMap() {
    return positionsSortMap;
  }

  /* ------------------ Методы ------------------ */
  private static List<CompetitorClass>
  pointsSortUp(List<CompetitorClass> list) {
    // Сортируем по очкам
    List<CompetitorClass> response = new LinkedList<>();
    int indexMax = 0;
    while (list.size() > 0) {
      double maxValue = 0;
      indexMax = 0;
      for (int i = 0; i < list.size(); i++) {
        if (maxValue < list.get(i).getPositionsWeight()) {
          maxValue = list.get(i).getPositionsWeight();
          indexMax = i;
        }
      }
      response.add(list.get(indexMax));
      list.remove(indexMax);
    }
    return response;
  }

  private static int checkByBestPlaces(List<CompetitorClass> list) {
    // Подсчитываем количecтво лучших мест
    int pos = 0;
    for (int position = 1; position < maxStartPosition; position++) {
      if (list.get(0).getPlaceCount(position) !=
          list.get(1).getPlaceCount(position)) {
        pos = 20 + position;
        break;
      }
    }
    return pos;
  }

  private static List<CompetitorClass> lastSort(List<CompetitorClass> list) {

    // Сортируем с последнего заезда
    List<List<CompetitorClass>> byHeatsSortResponse = new LinkedList<>();
    byHeatsSortResponse.add(list);

    // Двигаемся по хитам с конца
    for (int heat = heatCount - 1; heat >= 0; heat--) {
      List<List<CompetitorClass>> temp = new LinkedList<>();
      temp.addAll(byHeatsSortResponse);

      byHeatsSortResponse.clear();

      for (int rows = 0; rows < temp.size(); rows++) {
        List<List<CompetitorClass>> bufList = new LinkedList<>();
        // Если больше одной строки осталось, то отправляем на сортировку
        if (temp.get(rows).size() > 1) {
          bufList = byHeatsSort(temp.get(rows), heat);
          for (int i = 0; i < bufList.size(); i++) {
            byHeatsSortResponse.add(bufList.get(i));
          }
        } else {
          byHeatsSortResponse.add(temp.get(rows));
        }
      }
    }

    //
    List<CompetitorClass> response = new LinkedList<>();
    for (int m = 0; m < byHeatsSortResponse.size(); m++) {
      for (int l = 0; l < byHeatsSortResponse.get(m).size(); l++) {
        response.add(byHeatsSortResponse.get(m).get(l));
      }
    }
    return response;
  }

  private static List<List<CompetitorClass>>

  byHeatsSort(List<CompetitorClass> list, int heat) {
    // Сортируем с указанному заезду
    List<List<CompetitorClass>> response = new LinkedList<>();

    for (int pos = 1; pos <= maxStartPosition + 1; pos++) {

      // на случай если есть нулевые позиции
      int position = 0;
      if (pos == maxStartPosition + 1)
        position = 0;
      else
        position = pos;

      if (list.size() == 0)
        break;
      List<CompetitorClass> sort = new LinkedList<>();
      boolean done = false;
      while (!done) {
        done = true;
        for (int index = 0; index < list.size(); index++) {
          if (list.get(index).getHeats()[heat] == position) {
            sort.add(list.get(index));
            list.remove(index);
            done = false;
          }
        }
      }
      if (sort.size() > 0) {
        if (sort.size() == 1) {
          //.   Код - Сортировка по:.................
          //... 3x - сортировка с конца в начало (32 - по 2 столбцу)
          //... 40 - сортировка по последнему заезду

          // Если один в листе, значит отмечаем
          sort.get(0).setCode(30 + heat + 1);
        } else {
          // Сортировка по последнему заезду
          sort = byLastHeatSort(sort);
        }
        response.add(sort);
      }
    }

    return response;
  }

  private static List<CompetitorClass>
  byLastHeatSort(List<CompetitorClass> list) {
    List<CompetitorClass> tempResultList = new LinkedList<>();
    int indexMax = 0;
    while (list.size() > 0) {
      int maxValue = 0;
      indexMax = 0;
      for (int i = 0; i < list.size(); i++) {
        if (maxValue < list.get(i).getLastHeatNum()) {
          maxValue = list.get(i).getLastHeatNum();
          indexMax = i;
        }
      }
      //.   Код - Сортировка по:.................
      //... 40 - сортировка по последнему заезду
      list.get(indexMax).setCode(40);
      tempResultList.add(list.get(indexMax));
      list.remove(indexMax);
    }
    return tempResultList;
  }
}
