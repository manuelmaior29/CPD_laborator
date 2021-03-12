#include <iostream>
#include "omp.h"
#include <sys/utime.h>

#define SIZE 200
#define DO_PRINT false

int main()
{
    float A[SIZE][SIZE], b[SIZE][SIZE], c[SIZE][SIZE], total;
    int i, j, k, tid;
    total = 0.0;
    for (i = 0; i < SIZE; i++)
    {
        for (j = 0; j < SIZE; j++)
        {
            A[i][j] = (j + 1) * 1.0;
            b[i][j] = (j + 1) * 1.0;
            c[i][j] = 0.0;
        }
    }

    printf("\nStarting values of matrix A and matrix b:\n");

    if (DO_PRINT)
    {
        for (i = 0; i < SIZE; i++)
        {
            printf(" A[%d]= ", i);
            for (j = 0; j < SIZE; j++)
            {
                printf("%.1f ", A[i][j]);
            }
            printf("\n");
        }
        printf("\n");
        for (i = 0; i < SIZE; i++)
        {
            printf(" b[%d]= ", i);
            for (j = 0; j < SIZE; j++)
            {
                printf("%.1f ", b[i][j]);
            }
            printf("\n");
        }
    }

    printf("\nResults by thread/row:\n");

    double stime = omp_get_wtime();
    /* Create a team of threads and scope variables */
#pragma omp parallel for private(i, j, k) shared(A, b, c)
    for (i = 0; i < SIZE; i++)
    {
        for (j = 0; j < SIZE; j++)
        {
            for (k = 0; k < SIZE; k++)
            {
                c[i][j] += (A[i][k] * b[k][j]);
            }
        }
    }
    double etime = omp_get_wtime();
    printf("\nTime taken for matrix multiplication: %.8f.\n", etime - stime);

    if (DO_PRINT)
        for (i = 0; i < SIZE; i++)
        {
            printf(" c[%d]= ", i);
            for (j = 0; j < SIZE; j++)
            {
                printf("%.1f ", c[i][j]);\
            }
            printf("\n");
        }

    printf("\nMatrix-vector total - sum of all c[] = %.2f\n\n", total);
}