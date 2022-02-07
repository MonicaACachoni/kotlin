package Malha;
//package br.com.cpqd.router.algds;

import java.lang.Math;


/**
   Indexed binary heap where each node stores a pair {key,cost}. Heap property
   is minimum on costs.  Keys in the heap must be distinct in the range
   [0,n). Weights may be update through key.

   <p>This implementation is addressed to be used with Dijkstra, Prim and A*
   algorithms minimizing performance overhead.

   <p>For more general use the arrays holding the heap and its index could be
   replaced by Vectors, a constructor that does not define heap size in advance
   could be provided, and the insert method should be modified accordingly.  The
   cost will be a time and space overhead introduced by Vector function calls
   and allocation strategy.  Also, the methods could be made safer, checking for
   heap bounds and key absence, at the cost of performance overhead.  In this
   case a value for a deleted key, for instance -1, should be stored in the
   index to allow testing key absence from the heap.

   @author g.p.telles, 14sep07.
*/


public class Heap {

  // The nodes in the heap:
  private class node {
    public int k;
    public double c;
  }

  private node[] heap;
  private int size;

  // The index to the nodes in the heap:
  private int[] index;




  /********************************************************************************
     Creates an empty heap for at most n pairs {key,cost}. Keys to be
     inserted in the heap must be distinct in the range [0,n).
  ********************************************************************************/
  Heap(int n) {
    heap = new node[n];
    index = new int[n];
    size = 0;
  }





  /********************************************************************************
     Inserts a pair {key,cost} in the heap.  k is not checked for heap
     bounds and k is not checked for existence in the heap.
  ********************************************************************************/
  public void insert(int k, double c) {

    if (heap[size] == null)
      heap[size] = new node();
    heap[size].k = k;
    heap[size].c = c;
    index[k] = size;

    heapfyUp(size++);
  }




  /********************************************************************************
     Moves the node at position i up in the heap.  i is not tested for
     heap bounds.
  ********************************************************************************/
  private void heapfyUp(int i) {

    int p;
    node auxn;

    // Evaluates the index of i's parent:
    p = (int) Math.floor((i-1)/2);

    while (i>0 && heap[i].c < heap[p].c) {
      index[heap[i].k] = p;
      index[heap[p].k] = i;

      auxn = heap[i];
      heap[i] = heap[p];
      heap[p] = auxn;

      i = p;
      p = (int) Math.floor((i-1)/2);
    }
  }





  /********************************************************************************
     Moves the node at position i down in the heap.  i is not tested
     for heap bounds.
  ********************************************************************************/
  private void heapfyDown(int i) {

    int l,r,s;
    node aux;

    // Children of i:
    l = 2*i+1;
    r = l+1;

    // Selects the child s of i with smaller cost:
    if (r<=size-1) 
      s = heap[l].c < heap[r].c ? l : r;
    else if (l==size-1)
      s = l;
    else
      return;

    while (heap[i].c > heap[s].c) {

      index[heap[i].k] = s;
      index[heap[s].k] = i;

      aux = heap[i];
      heap[i] = heap[s];
      heap[s] = aux;

      i = s;
      l = 2*i+1;
      r = l+1;

      if (r<=size-1) 
        s = heap[l].c < heap[r].c ? l : r;
      else if (l==size-1)
        s = l;
      else
        return;
    }
  }





  /********************************************************************************
     Removes and returns the key with the minimum cost and adjusts the
     heap.  The heap is not tested for emptiness.
  ********************************************************************************/
  public int deleteMin() {

    int k = heap[0].k;

    if (--size>0) {
      heap[0].k = heap[size].k;
      heap[0].c = heap[size].c;
      index[heap[0].k] = 0;
      //heap[size] = null;
      heapfyDown(0);
    }

    return k;
  }


public boolean exists(int k) {
	return index[k] == 0 ? false : true;
}


  /********************************************************************************
     Updates the cost of key k to c and adjusts the heap.  k is not
     checked for heap bounds, k is not checked for existence in the
     heap.
  ********************************************************************************/
  public void updateCost(int k, double c) {

    if (heap[index[k]].c > c) {
      heap[index[k]].c = c;
      heapfyUp(index[k]);
    }
    else {
      heap[index[k]].c = c;
      heapfyDown(index[k]);
    }
  }





  /********************************************************************************
     Decreases the cost of key k to c and adjusts the heap.  k is not
     checked for heap bounds, k is not checked for existence in the
     heap, c is not checked to be smaller or equal to the current
     value.
  ********************************************************************************/
  public void decreaseCost(int k, double c) {

    heap[index[k]].c = c;
    heapfyUp(index[k]);
  }




  /********************************************************************************
  ********************************************************************************/
  public int size() {
    return size;
  }





  /********************************************************************************
     For testing.
  ********************************************************************************/
  public void print() {

    double x;

    for (int i=2; i<size+2; i++) {
      System.out.printf("%d,",heap[i-2].k);
      System.out.printf("%.2f ",heap[i-2].c);

      x = Math.log10(i)/Math.log10(2);
      if (Math.floor(x) == x) {
        System.out.printf("\n");
      }
    }
    System.out.printf("\n\n");

    for (int i=0; i<size; i++) {
      System.out.printf("%d,%d  ",heap[i].k,index[heap[i].k]);
    }
    System.out.printf("\n\n");
  }
}  

