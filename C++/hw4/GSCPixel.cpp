#include <iostream>
#include <cmath>
#include "GSCPixel.hpp"

using namespace std;



// Constructor based on other pixel
GSCPixel::GSCPixel(const GSCPixel& p) {
    value = p.value;
}

// Constructor based on gsc values
GSCPixel::GSCPixel(unsigned char c) {
      value = c;
}


unsigned char GSCPixel::getValue() {
    return value;
}


void GSCPixel::setValue(unsigned char value) {
    value = value;
}