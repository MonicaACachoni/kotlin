
#include <math.h>

int main(int argc, char** argv) {

  float f = INFINITY;

printf("%d\n", isfinite(f));

  f = 0.0;

printf("%d\n", isfinite(f));

  return 0;
}
