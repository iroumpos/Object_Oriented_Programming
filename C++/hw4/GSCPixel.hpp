#ifndef GSCPIXEL_HPP
#define GSCPIXEL_HPP

#include "Pixel.hpp"

class GSCPixel : public Pixel{
private:
    unsigned char value;    // value of grayscale pixel
public:
    GSCPixel() = default;
    GSCPixel(const GSCPixel& p);
    GSCPixel(unsigned char c);
    unsigned char getValue();
    void setValue(unsigned char value);

};


#endif
