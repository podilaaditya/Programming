#include <iostream>
using namespace std;

template <typename T, size_t n>
void insertionSort(T (&arrayToSort) [n]) {
  int i, j;
  T key;
  int size = sizeof(T) * n;
  cout << "Sorting the Array given \n";
  cout << "Length of given Array " << n << "\n";
  for (i = 1; i < n; i++)
  {
       key = arrayToSort[i];
       j = i-1;

       /* Move elements of arr[0..i-1], that are
          greater than key, to one position ahead
          of their current position */
       while (j >= 0 && arrayToSort[j] > key)
       {
           arrayToSort[j+1] = arrayToSort[j];
           j = j-1;
       }
       arrayToSort[j+1] = key;
  }
}

void swap(int *xp, int *yp)
{
    int temp = *xp;
    *xp = *yp;
    *yp = temp;
}

template <typename T, size_t n>
void selectionSort(T(&arrayToSort)[n]) {
  int len = n;
  int min_idx = 0;
  for(int cntr = 0;  cntr < len ;  cntr++ ) {
    min_idx = cntr;
    for(int sub_array_cntr = 0; sub_array_cntr < len ; sub_array_cntr++) {
      if (arrayToSort[sub_array_cntr] < arrayToSort[min_idx]) {
        min_idx = sub_array_cntr;
      }
    swap(&arrayToSort[min_idx],&arrayToSort[cntr]) ;
    }
  }
}


template <typename T, size_t n>
void printArray(T (&arrayToSort) [n]) {

  for (int i = 0; i < n; i++)
  {
    cout << arrayToSort[i] << '\t';
  }

  cout << '\n';
}




/* C program for Merge Sort */
// Merges two subarrays of arr[].
// First subarray is arr[l..m]
// Second subarray is arr[m+1..r]
template <typename T, size_t n>
void merge(T (&arrayToSort)[n], int l, int m, int r)
{
    int i, j, k;
    int n1 = m - l + 1;
    int n2 = r - m;

    /* create temp arrays */
    int L[n1], R[n2];

    /* Copy data to temp arrays L[] and R[] */
    for (i = 0; i < n1; i++)
        L[i] = arrayToSort[l + i];
    for (j = 0; j < n2; j++)
        R[j] = arrayToSort[m + 1 + j];

    /* Merge the temp arrays back into arr[l..r]*/
    i = 0; // Initial index of first subarray
    j = 0; // Initial index of second subarray
    k = l; // Initial index of merged subarray
    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) {
            arrayToSort[k] = L[i];
            i++;
        }
        else {
            arrayToSort[k] = R[j];
            j++;
        }
        k++;
    }

    /* Copy the remaining elements of L[], if there
       are any */
    while (i < n1) {
        arrayToSort[k] = L[i];
        i++;
        k++;
    }

    /* Copy the remaining elements of R[], if there
       are any */
    while (j < n2) {
        arrayToSort[k] = R[j];
        j++;
        k++;
    }
}

/* l is for left index and r is right index of the
   sub-array of arr to be sorted */
template <typename T, size_t n>
void mergeSort(T (&arrayToSort)[n], int l, int r)
{
    if (l < r) {
        // Same as (l+r)/2, but avoids overflow for
        // large l and h
        int m = l + (r - l) / 2;
        // Sort first and second halves
        mergeSort(arrayToSort, l, m);
        mergeSort(arrayToSort, m + 1, r);
        merge(arrayToSort, l, m, r);
    }
}
/* This function takes last element as pivot, places
   the pivot element at its correct position in sorted
   array, and places all smaller (smaller than pivot)
   to left of pivot and all greater elements to right
   of pivot */
int partition (int arr[], int low, int high)
{
  cout << "partition low :: " << low << "\n";
  cout << "partition high :: " << high  << "\n";

    int pivot = arr[high];    // pivot
    int i = (low - 1);  // Index of smaller element

    for (int j = low; j <= high- 1; j++)
    {
        // If current element is smaller than or
        // equal to pivot
        if (arr[j] <= pivot)
        {
            i++;    // increment index of smaller element
            swap(&arr[i], &arr[j]);
        }
    }

    swap(&arr[i + 1], &arr[high]);
    return (i + 1);
}

/*
   The main function that implements QuickSort
   arr[] --> Array to be sorted,
   low  --> Starting index,
   high  --> Ending index
*/
void quickSort(int arr[], int low, int high)
{
  cout << "low :: " << low  << "\n";
  cout << "high :: " << high  << "\n";
    if (low < high)
    {
        /*
           pi is partitioning index, arr[p] is now
           at right place
        */
        int pi = partition(arr, low, high);
        // Separately sort elements before
        // partition and after partition
        quickSort(arr, low, pi - 1);
        //printArray(arr);
        quickSort(arr, pi + 1, high);
        //printArray(arr);
    }
}


int main (void) {

  int a[10] = {10,9,8,7,6,1,2,3,4,5};
  int b[10] = {10,9,8,7,6,1,2,3,4,5};
  int c[11] = {10,9,8,7,6,85,84,83,82,81,80};
  int d[11] = {11,19,18,17,16,5,4,2,1,2,0};
  int arr_size = sizeof(c) / sizeof(b[0]);
  cout << "given array --A[10] insertionSort \n";
  printArray(a);
  insertionSort(a);
  printArray(a);
  cout <<"\n ======= \n ";
  cout << "given array --B[10] selectionSort \n";
  printArray(b);
  selectionSort(b);
  printArray(b);

  cout <<"\n ======= \n ";
  cout << "given array --c[10] mergeSort \n";
  printArray(c);
  mergeSort(c, 0, arr_size - 1);
  printArray(c);

  cout <<"\n ======= \n ";
  cout << "given array --c[10] mergeSort \n";
  printArray(d);
  quickSort(d, 0, 11-1);
  printArray(d);

  return 0;
}
