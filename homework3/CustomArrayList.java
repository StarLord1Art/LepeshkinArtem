public class CustomArrayList<T> implements Interface.Methods<T> {

  private final int INIT_SIZE = 16;
  private final int CUT_RATE = 4;
  private Object[] array = new Object[INIT_SIZE];
  private int pointer = 0;

  /*
  Добавляет новый элемент в список. При достижении capacity происходит её увеличение в два раза.
  */
  public void add(T item) {
    if (item == null) {
      throw new NullPointerException("Аргумент не может быть null.");
    }
    if (pointer == array.length - 1) {
      resize(array.length * 2); // увеличиваем capacity в 2 раза, если достигли границы
    }
    array[pointer++] = item;
  }

  /*
  Возвращает элемент списка по индексу.
  */
  public T get(int index) {
    return (T) array[index];
  }

  /*
  Удаляет элемент списка по индексу. Все элементы справа от удаляемого
  перемещаются на шаг влево. Если после удаления элемента количество
  элементов стало в CUT_RATE раз меньше чем capacity,
  то она уменьшается в два раза, для экономии занимаемого места.
  */
  public void remove(int index) {
    for (int i = index; i < pointer; i++) {
      array[i] = array[i + 1];
    }
    array[pointer] = null;
    pointer--;

    // если элементов в CUT_RATE раз меньше чем capacity, то уменьшаем её длину в два раза
    if (array.length > INIT_SIZE && pointer < array.length / CUT_RATE) {
      resize(array.length / 2);
    }
  }

  /*Вспомогательный метод для масштабирования.*/
  private void resize(int newLength) {
    Object[] newArray = new Object[newLength];
    System.arraycopy(array, 0, newArray, 0, pointer);
    array = newArray;
  }
}
