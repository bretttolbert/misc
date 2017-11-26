#ifndef HEAP_H
#define HEAP_H

#include <iostream>
#include <vector>
#include <cstdlib> //required for exit()
using namespace std;

template <class T>
class Heap
{
/*
A simple binary max-heap implementation with all of the operations
needed to implement heapsort. It uses a vector of type int to
store the elements of the heap. The indices of the vector
represent the nodes of the tree if they where numbered in the
order visited by breath first traversal, with the root node at
index 1 (index 0 has value 0 and is only a placeholder)
*/
private:
	vector<T> A;
    bool isMinHeap; //true if this is a min-heap, false (max-heap) by default
    void buildHeapFromVector(vector<T>);
    int parent(int);
    int lchild(int);
    int rchild(int);
    void percolateDown(int);
    void percolateUp(int);
    typedef typename std::vector<T>::reverse_iterator VectorReverseIterator;
    typedef typename std::vector<T>::iterator VectorIterator;
public:
    Heap(bool);
    Heap(vector<T>, bool);
    int size() const;
    bool empty() const;
    T pop();
    T peek() const;
    void push(T value);
    static void heapsort(vector<T> &, bool);
};

template <class T>
Heap<T>::Heap(bool isMinHeap=false)
{
    //This heap can be either a max-heap, where the root node always
    //has the maximum value or a min-heap where the root node always
    //has the minimum value. By default, the heap will be a max-heap.
    this->isMinHeap = isMinHeap;

	//A[0] = 0, this is not an element of the heap
	//It is only a placeholder so indexing will start at 1.
    //This seems like a waste of space but it helps keep the implementation simple.
    //For example, parent(int) returns the index of the parent of a node,
    //but if parent is called for the root node (at index 1) it returns 0 (false).
    //Similarly, lchild(int) and rchild(int) will return 0 (false) if a node doesn't
    //have a left child or right child (respectively).
	A.push_back(0);
}

/*
Builds a heap from a vector of unsorted elements using buildHeapFromVector().
*/
template <class T>
Heap<T>::Heap(vector<T> elements, bool isMinHeap=false)
{
    //This heap can be either a max-heap, where the root node always
    //has the maximum value or a min-heap where the root node always
    //has the minimum value. By default, the heap will be a max-heap.
    this->isMinHeap = isMinHeap;

    //Contruct the heap from an existing unsorted vector of elements.
    buildHeapFromVector(elements);
}

/*
Returns the index of the parent of the node at the given index
or returns 0 (false) if the node has no parent (is the root node).
*/
template <class T>
int Heap<T>::parent(int i)
{
	//returns index of parent of i or 0 if i is root
	if (i != 1)
		return i/2;
	return 0;
}

/*
Returns the index of the left child of the node at the given index
or 0 (false) if the node has no left child (is a leaf node).
If a node doesn't have a left child, it is safe to assume that
it doesn't have a right child either as rows in the tree are
filled from left to right.
*/
template <class T>
int Heap<T>::lchild(int i)
{
	//returns index of lchild of i or 0 if i has no lchild
	if (2*i < A.size())
		return 2*i;
	else
		return 0;
}

/*
Returns the index of the right child of the node at the given index
or 0 (false) if the node has no right child.
Note that not having a right child doesn't necessarily mean that
a node is a leaf node as it may have a left child.
*/
template <class T>
int Heap<T>::rchild(int i)
{
	//returns index of rchild of i or 0 if i has no rchild
	if (2*i+1 < A.size())
		return 2*i+1;
	else
		return 0;
}

/*
Returns the number of nodes in the heap.
*/
template <class T>
int Heap<T>::size() const
{
	//returns the number of elements in the heap
    //This is equal to the size of the vector A minus 1 
    //because the first element is only a placeholder.
	return A.size()-1;
}

/*
Returns true if heap is empty.
*/
template <class T>
bool Heap<T>::empty() const
{
    return A.size() < 2;
}

/*
Percolate Down (AKA Heapify Down).
Lets i float down the tree until the heap property is satisfied.
It assumes the the subtrees rooted at the right and left child of
i already satisfy the heap property.
This function would normally be used to restore the heap after the
root node is deleted.
The Algorithm:
Swaps i with its largest child if largest child > i
then calls itself on largest child.
Recursion ends when bottom of tree is reached.
*/
template <class T>
void Heap<T>::percolateDown(int i)
{
	int l = lchild(i);
	int r = rchild(i);
    int largestOrSmallest;

    //If the heap is a max heap, determine whether the parent, left child,
    //or right child is the largest.
    if (!isMinHeap)
    {
	    int largest = i;
	    if (l && A[l] > A[i])
		    largest = l;
	    if (r && A[r] > A[largest])
		    largest = r;
        largestOrSmallest = largest;
    }
    //If the heap is a min heap, determine whether the parent, left child,
    //or right child is the smallest.
    else
    {
	    int smallest = i;
	    if (l && A[i] > A[l])
		    smallest = l;
	    if (r && A[smallest] > A[r])
		    smallest = r;
        largestOrSmallest = smallest;
    }

    //If the largest (or smallest) is not the parent, swap
    //the parent with its largest (or smallest) child.
    if (largestOrSmallest != i)
    {
	    //swap i and largest (or smallest)
	    T temp = A[largestOrSmallest];
	    A[largestOrSmallest] = A[i];
	    A[i] = temp;
	    //call percolateDown on the largest (or smallest) subtree
	    percolateDown(largestOrSmallest);
    }
}

/*
Percolate Up (AKA Heapify Up)
Lets i float up the tree until the heap property is satisfied.
Swaps i with its parent if parent(i) < i then recursively calls
percolateUp() on its parent. Recursion ends when no swap
needs to be made or the top of the tree is reached, whichever
happens first. percolateUp() is always called on the last
element in the vector after an insert operation.
*/
template <class T>
void Heap<T>::percolateUp(int i)
{
    //Swap if i is not the root node and i > parent(i) in the case of a max heap
    //or if parent(i) > i in the case of a min heap.
    if (i!=1 && 
        ((!isMinHeap && A[i] > A[parent(i)]) || (isMinHeap && A[parent(i)] > A[i])))
    {
        //swap i with its parent
        T temp = A[parent(i)];
        A[parent(i)] = A[i];
        A[i] = temp;
        //recursively call percolateUp on parent
        percolateUp(parent(i));
    }
}

/*
removes the root element and returns its value
replaces root with the last element
restores the heap by calling percolateDown on root
*/
template <class T>
T Heap<T>::pop()
{
	//if the heap is empty, show error message
	if (A.size() < 2)
	{
		cout << "pop_root() error: empty heap\n";
		return -1;
	}

	//if the heap has only one element, remove root and return
	else if (A.size() == 2)
	{
		T root = A.back();
		A.pop_back();
		return root;
	}
	else
	{
		//save value of root
		T old_root = A[1];
		//replace root with last element
		A[1] = A.back();
		A.pop_back();
		//call hepify_down on root to restore heap order
		percolateDown(1);
		return old_root;
	}
}

/*
Returns the value of the root node.
Does not modify the heap.
*/
template <class T>
T Heap<T>::peek() const
{
    if (A.size() < 2)
    {
        cout << "Error: peek() failed because heap is empty.\n";
        exit(1);
    }
    return A[1];
}

/*
Pushes a new element onto the Heap and restores the heap
property using percolateUp()
*/
template <class T>
void Heap<T>::push(T value)
{
    A.push_back(value);
    percolateUp(A.size()-1);
}

/*
Builds a heap from a vector of unsorted elements.
Replaces A with the list of elements then calls
percolateDown on each node which is not a leaf.
Intially calls percolateDown on the last node which is 
not a leaf, then calls percolateDown on the preceding node
repeatedly until the root node is reached.
percolateDown is called on the root node as well.
After that, the heap property will be satisfied.
*/
template <class T>
void Heap<T>::buildHeapFromVector(vector<T> elements)
{
	A.clear();
	A.push_back(0);
	for (int i=0; i<elements.size(); i++)
		A.push_back(elements[i]);
	//start at end of A and work backwards until we find a node
	//which is not a leaf node (has at least one child)
	int i = A.size()-1;
	while (!lchild(i) && !rchild(i))
		i -= 1;
	//call percolateDown on i and every node before i
	while (i > 0)
	{
		percolateDown(i);
		i -= 1;
	} 
}

/**
 * Static member function of Heap class.
 * Performs heapsort on a vector of elements by constructing a heap
 * from the list then continuously popping the root element
 * and placing it appropriately back into the vector.
 *
 * It sorts the elements in ascending order by default but will sort
 * in descending order if the descending param is true.
 * A max-heap will be used for both ascending and descending sort.
 * For ascending sort, the elements will be added to the vector from 
 * back to front and for descending sort, they will be added from front
 * to back.
 *
 * @param list the vector of elements to be sorted, passed by reference  
 * @param descending true for descending order, false by default (ascending)
 */
template <class T>
void Heap<T>::heapsort(vector<T> &list, bool descending=false)
{
    Heap h(list);

    if (descending)
    {
        for (VectorReverseIterator it = list.rbegin(); it != list.rend(); ++it)
                *it = h.pop();
    }
    else
    {
        for (VectorIterator it = list.begin(); it != list.end(); ++it)
            *it = h.pop();
    }
}

#endif
