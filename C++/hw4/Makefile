# Compiler and Flags
CXX = g++
CXXFLAGS = -std=c++11 -Wall -fsanitize=address

# Source Files
SRCS = hw4.cpp RGBPixel.cpp RGBImage.cpp GSCPixel.cpp GSCImage.cpp Token.cpp

# Object Files
OBJS = $(SRCS:.cpp=.o)

# Executable Name
EXEC = hw4

# Default Target
all: $(EXEC)

# Linking
$(EXEC): $(OBJS)
	$(CXX) $(CXXFLAGS) $(OBJS) -o $(EXEC)

# Compilation
.cpp.o:
	$(CXX) $(CXXFLAGS) -c $< -o $@

# Clean
clean:
	rm -f $(OBJS) $(EXEC)

	