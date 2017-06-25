#include <iostream>
#include <vector>
#include <cstdlib> //needed for rand()
#include "Heap.h"
using namespace std;

int main()
{
    srand(time(0));

	Heap<int> h(true);
    h.push(14);
    h.push(19);
    h.push(12);
    h.push(7);
    h.push(18);
    h.push(15);
    h.push(4);
    h.push(8);

    int x = h.pop();
    cout << x << endl;

    
    
	return 0;
}
