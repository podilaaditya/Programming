/**
 * File: omxIC.h
 * Brief: OpenMAX DL v1.0.2 - Image Coding library
 *
 * Copyright © 2005-2008 The Khronos Group Inc. All Rights Reserved. 
 *
 * These materials are protected by copyright laws and contain material 
 * proprietary to the Khronos Group, Inc.  You may use these materials 
 * for implementing Khronos specifications, without altering or removing 
 * any trademark, copyright or other notice from the specification.
 * 
 * Khronos Group makes no, and expressly disclaims any, representations 
 * or warranties, express or implied, regarding these materials, including, 
 * without limitation, any implied warranties of merchantability or fitness 
 * for a particular purpose or non-infringement of any intellectual property. 
 * Khronos Group makes no, and expressly disclaims any, warranties, express 
 * or implied, regarding the correctness, accuracy, completeness, timeliness, 
 * and reliability of these materials. 
 *
 * Under no circumstances will the Khronos Group, or any of its Promoters, 
 * Contributors or Members or their respective partners, officers, directors, 
 * employees, agents or representatives be liable for any damages, whether 
 * direct, indirect, special or consequential damages for lost revenues, 
 * lost profits, or otherwise, arising from or in connection with these 
 * materials.
 * 
 * Khronos and OpenMAX are trademarks of the Khronos Group Inc. 
 *
 */

/* *****************************************************************************************/

#ifndef _OMXIC_H_
#define _OMXIC_H_

#include "omxtypes.h"

#ifdef __cplusplus
extern "C" {
#endif


/* 5.1.2.2 Vendor-Specific Data Structures */
 typedef void OMXICJPHuffmanEncodeSpec;
 typedef void OMXICJPHuffmanDecodeSpec;


/* 5.1.2.1 JPEG Markers  */
/* omxICJP_Marker, a data type that enumerates JPEG markers, is defined as follows:  */

typedef enum {
    omxICJP_Marker_TEM   = 1,
    omxICJP_Marker_SOF0  = 192,
    omxICJP_Marker_SOF1  = 193,
    omxICJP_Marker_SOF2  = 194,
    omxICJP_Marker_SOF3  = 195,
    omxICJP_Marker_DHT   = 196,
    omxICJP_Marker_SOF5  = 197,
    omxICJP_Marker_SOF6  = 198,
    omxICJP_Marker_SOF7  = 199,
    omxICJP_Marker_JPG   = 200,
    omxICJP_Marker_SOF9  = 201,
    omxICJP_Marker_SOF10 = 202,
    omxICJP_Marker_SOF11 = 203,
    omxICJP_Marker_DAC   = 204,
    omxICJP_Marker_SOF13 = 205,
    omxICJP_Marker_SOF14 = 206,
    omxICJP_Marker_SOF15 = 207,
    omxICJP_Marker_RST0  = 208,
    omxICJP_Marker_RST1  = 209,
    omxICJP_Marker_RST2  = 210,
    omxICJP_Marker_RST3  = 211,
    omxICJP_Marker_RST4  = 212,
    omxICJP_Marker_RST5  = 213,
    omxICJP_Marker_RST6  = 214,
    omxICJP_Marker_RST7  = 215,
    omxICJP_Marker_SOI   = 216,
    omxICJP_Marker_EOI   = 217,
    omxICJP_Marker_SOS   = 218,
    omxICJP_Marker_DQT   = 219,
    omxICJP_Marker_DNL   = 220,
    omxICJP_Marker_DRI   = 221,
    omxICJP_Marker_DHP   = 222,
    omxICJP_Marker_EXP   = 223,
    omxICJP_Marker_APP0  = 224,
    omxICJP_Marker_APP1  = 225,
    omxICJP_Marker_APP2  = 226,
    omxICJP_Marker_APP3  = 227,
    omxICJP_Marker_APP4  = 228,
    omxICJP_Marker_APP5  = 229,
    omxICJP_Marker_APP6  = 230,
    omxICJP_Marker_APP7  = 231,
    omxICJP_Marker_APP8  = 232,
    omxICJP_Marker_APP9  = 233,
    omxICJP_Marker_APP10 = 234,
    omxICJP_Marker_APP11 = 235,
    omxICJP_Marker_APP12 = 236,
    omxICJP_Marker_APP13 = 237,
    omxICJP_Marker_APP14 = 238,
    omxICJP_Marker_APP15 = 239,
    omxICJP_Marker_JPG0  = 240,
    omxICJP_Marker_JPG1  = 241,
    omxICJP_Marker_JPG2  = 242,
    omxICJP_Marker_JPG3  = 243,
    omxICJP_Marker_JPG4  = 244,
    omxICJP_Marker_JPG5  = 245,
    omxICJP_Marker_JPG6  = 246,
    omxICJP_Marker_JPG7  = 247,
    omxICJP_Marker_JPG8  = 248,
    omxICJP_Marker_JPG9  = 249,
    omxICJP_Marker_JPG10 = 250,
    omxICJP_Marker_JPG11 = 251,
    omxICJP_Marker_JPG12 = 252,
    omxICJP_Marker_JPG13 = 253,
    omxICJP_Marker_COM   = 254 
} omxICJP_Marker;



/**
 * Function:  omxICJP_CopyExpand_U8_C3   (5.1.3.1.1)
 *
 * Description:
 * This function copies an image block from the source image buffer to the 
 * destination image buffer.  If the size of the destination block is larger 
 * than the size of the source block, then the function affects pixel 
 * expansion by filling extra space in the destination block with copies of 
 * pixel values from the edges of the source block. This function is intended 
 * for use in the JPEG encoder for completion of partial MCUs (e.g., BGR8:8:8, 
 * RGB8:8:8) along the vertical and horizontal image boundaries prior to 
 * conversion to the target encoded color space.  As such, the destination 
 * block must be of size larger than or equal to the source block, and must 
 * have standard JPEG MCU [ISO10918-1] dimensions of either 8x8, 8x16, 16x8, 
 * or 16x16 horizontal by vertical pixels. Both top-down and bottom-up storage 
 * are supported, and are indicated, respectively, via positive and negative 
 * values for the parameters srcStep/dstStep. For example, given positive 
 * source and destination step values (top-down source and destination 
 * images), the function copies the source block to the destination block, and 
 * pads the the extra space in the destination block to the right of the 
 * source block boundary with copies of the right-most pixel from the source 
 * block on each scanline. Similarly, any extra space in the larger 
 * destination block below the source block boundary is padded with copies of 
 * the bottom-most pixel from the source block. This function supports only 
 * interleaved (C3) image blocks. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source block 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image; a positive value may be used to indicate 
 *            top-down storage, or a negative value may be used to indicate 
 *            bottom-up storage 
 *   srcSize - size of the source block 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image; a positive value may be used to indicate 
 *            top-down storage, or a negative value may be used to indicate 
 *            bottom-up storage 
 *   dstSize - size of destination block; if dstSize > srcSize then pixel 
 *            expansion is applied. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination buffer 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments; returned under one or more of the 
 *              following conditions: a pointer was NULL 
 *    -   one of the source or destination region rectangle dimensions was 
 *              equal to 0 dstSize is not equal to one of the following: 
 *              {8,8}, {8,16}, {16,8}, or {16,16}. srcSize.width > 
 *              dstSize.width. srcSize.height > dstSize.height. 
 *    -   either |srcStep| < 3 or |dstStep| < 3 
 *
 */
OMXResult omxICJP_CopyExpand_U8_C3 (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMXSize srcSize,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize dstSize
);



/**
 * Function:  omxICJP_DCTQuantFwdTableInit   (5.1.3.2.1)
 *
 * Description:
 * Initializes the JPEG DCT quantization table for 8-bit per component image 
 * data. 
 *
 * Input Arguments:
 *   
 *   pQuantRawTable - pointer to the raw quantization table; must be arranged 
 *            in raster scan order and aligned on an 8-byte boundary.  The 
 *            table must contain 64 entries.  Table entries should lie within 
 *            the range [1,255]. 
 *
 * Output Arguments:
 *   
 *   pQuantFwdTable - pointer to the initialized quantization table; must be 
 *            aligned on an 8-byte boundary.  The table must contain 64 
 *            entries, and the implementation-specific contents must match the 
 *            table contents expected by the associated set of forward DCT 
 *            quantization functions in the same OpenMAX DL implementation, 
 *            including the functions DCTQuantFwd_S16, DCTQuantFwd_S16_I, and 
 *            DCTQuantFwd_Multiple_S16. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments.  Returned for any of the following 
 *              conditions: a pointer was NULL the start address of a pointer 
 *              was not 8-byte aligned. 
 *    -    one or more of the entries is equal to 0 in the table referenced 
 *              by pQuantRawTable. 
 *
 */
OMXResult omxICJP_DCTQuantFwdTableInit (
    const OMX_U8 *pQuantRawTable,
    OMX_U16 *pQuantFwdTable
);



/**
 * Function:  omxICJP_DCTQuantFwd_S16   (5.1.3.2.2)
 *
 * Description:
 * Computes the forward DCT and quantizes the output coefficients for 8-bit 
 * image data; processes a single 8x8 block. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the input data block (8x8) buffer; must be arranged in 
 *            raster scan order and 8-byte aligned. The input components 
 *            should be bounded on the interval [-128, 127]. 
 *   pQuantFwdTable - pointer to the 64-entry quantization table generated 
 *            using DCTQuantFwdTableInit; must be aligned on an 8-byte 
 *            boundary. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the output transform coefficient block (8x8) buffer; 
 *            must be 8-byte aligned.  The output 8x8 matrix is the transpose 
 *            of the explicit result; this transpose will be handled in 
 *            Huffman encoding. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -    a pointer was NULL 
 *    -    one of the following pointers not 8-byte aligned: pSrc, pDst, or 
 *              pQuantFwdTable. 
 *
 */
OMXResult omxICJP_DCTQuantFwd_S16 (
    const OMX_S16 *pSrc,
    OMX_S16 *pDst,
    const OMX_U16 *pQuantFwdTable
);



/**
 * Function:  omxICJP_DCTQuantFwd_S16_I   (5.1.3.2.3)
 *
 * Description:
 * Computes the forward DCT and quantizes the output coefficients for the 
 * 8-bit image data in-place; processes a single 8x8 block. 
 *
 * Input Arguments:
 *   
 *   pSrcDst - pointer to input data block (8x8) buffer for in-place 
 *            processing; must be arranged in raster scan order and 8-byte 
 *            aligned.  The input components should be bounded on the interval 
 *            [-128, 127]. 
 *   pQuantFwdTable - pointer to the 64 entry quantization table generated 
 *            using DCTQuantFwdTableInit; must be aligned on an 8-byte 
 *            boundary. 
 *
 * Output Arguments:
 *   
 *   pSrcDst - pointer to the in-place output coefficient block (8x8) buffer; 
 *            must be 8-byte aligned.  The output 8x8 matrix is the transpose 
 *            of the explicit result; this transpose will be handled in 
 *            Huffman encoding. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -    a pointer was NULL 
 *    -    one of the following pointers not 8-byte aligned: pSrcDst, 
 *              pQuantFwdTable. 
 *
 */
OMXResult omxICJP_DCTQuantFwd_S16_I (
    OMX_S16 *pSrcDst,
    const OMX_U16 *pQuantFwdTable
);



/**
 * Function:  omxICJP_DCTFwd_S16   (5.1.3.2.4)
 *
 * Description:
 * Performs an 8x8 block forward discrete cosine transform (DCT).  This 
 * function implements forward DCT for the 8-bit image data (packed into 
 * signed 16-bit).  The output matrix is the transpose of the explicit result. 
 *  As a result, the Huffman coding functions in this library handle transpose 
 * as well. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the input data block (8x8) buffer.  The data must be 
 *            arranged in raster scan order, and the buffer start address must 
 *            be 8-byte aligned.  The input components are bounded on the 
 *            interval [-128, 127]. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the output DCT coefficient block(8x8) buffer. This 
 *            start address must be 8-byte aligned. To achieve better 
 *            performance, the output 8x8 matrix is the transpose of the 
 *            explicit result. This transpose can be handled in later 
 *            processing stages (e.g. Huffman encoding). 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: a pointer was NULL the start address of a pointer 
 *              was not 8-byte aligned. 
 *
 */
OMXResult omxICJP_DCTFwd_S16 (
    const OMX_S16 *pSrc,
    OMX_S16 *pDst
);



/**
 * Function:  omxICJP_DCTFwd_S16_I   (5.1.3.2.5)
 *
 * Description:
 * This function implements forward DCT for the 8-bit image data (packed into 
 * signed 16-bit). It processes one block (8x8) in-place. The output matrix is 
 * the transpose of the explicit result.  As a result, the Huffman coding 
 * functions in this library handle transpose as well. 
 *
 * Input Arguments:
 *   
 *   pSrcDst - pointer to the input data block (8x8) buffer for in-place 
 *            processing.  The data must be arranged in raster scan order, and 
 *            the buffer base address must be 8-byte aligned.  The input 
 *            components are bounded on the interval [-128, 127] within a 
 *            16-bit container.  The output 8x8 matrix is the transpose of the 
 *            explicit result.  This transpose can be handled in later 
 *            processing stages (e.g. Huffman encoding). 
 *
 * Output Arguments:
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments.  Returned for any of the following 
 *              conditions: a pointer was NULL pSrcDst was not 8-byte aligned. 
 *
 */
OMXResult omxICJP_DCTFwd_S16_I (
    OMX_S16 *pSrcDst
);



/**
 * Function:  omxICJP_DCTQuantFwd_Multiple_S16   (5.1.3.2.6)
 *
 * Description:
 * This function implements forward DCT with quantization for 8-bit image 
 * data.  It processes multiple adjacent blocks (8x8). The blocks are assumed 
 * to be part of a planarized buffer.  This function should be called 
 * separately for luma and chroma buffers with the respective quantization 
 * table.  The output matrix is the transpose of the explicit result. As a 
 * result, the Huffman coding functions in this library handle transpose as 
 * well. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the start of the first 8x8 block in the input buffer, 
 *            which should contain nBlocks blocks of 8 x 8 image data.  The 
 *            data within each block must be arranged in raster scan order, 
 *            and the start address must be 8-byte aligned.  The input 
 *            components are bounded on the interval [-128, 127] within a 
 *            signed 16-bit container. Each 8x8 block in the buffer is stored 
 *            as 64 entries (16-bit) linearly in a buffer, and the multiple 
 *            blocks must be stored contiguously. 
 *   pQuantFwdTable - pointer to the 64-entry quantization table generated by 
 *            "DCTQuantFwdTableInit." must be 8-byte aligned. 
 *   nBlocks - the number of 8x8 blocks to be processed. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the start of the first 8x8 output coefficient block in 
 *            the multi-block coefficient output buffer.  The start address 
 *            must be 8-byte aligned.  To achieve better performance, the 
 *            output 8x8 matrices are the transpose of the explicit results 
 *            for each of the nBlocks blocks.  This transpose will be handled 
 *            in Huffman encoding. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -    a pointer was NULL 
 *    -    one of the following pointers not 8-byte aligned: pSrc, pDst, or 
 *              pQuantFwdTable. 
 *
 */
OMXResult omxICJP_DCTQuantFwd_Multiple_S16 (
    const OMX_S16 *pSrc,
    OMX_S16 *pDst,
    OMX_INT nBlocks,
    const OMX_U16 *pQuantFwdTable
);



/**
 * Function:  omxICJP_DCTQuantInvTableInit   (5.1.3.3.1)
 *
 * Description:
 * Initializes the JPEG IDCT inverse quantization table for 8-bit image data. 
 *
 * Input Arguments:
 *   
 *   pQuantRawTable - pointer to the raw (unprocessed) quantization table, 
 *            containing 64 entries; must be arranged in raster scan order and 
 *            aligned on an 8-byte boundary.  Table entries should lie within 
 *            the range [1,255]. 
 *
 * Output Arguments:
 *   
 *   pQuantInvTable - pointer to the initialized inverse quantization table; 
 *            must be aligned on an 8 byte boundary.  The table must contain 
 *            64 entries, and the implementation-specific contents must match 
 *            the table contents expected by the associated set of inverse DCT 
 *            quantization functions in the same OpenMAX DL implementation, 
 *            including the functions DCTQuantInv_S16, DCTQuantInv_S16_I, and 
 *            DCTQuantInv_Multiple_S16. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: a pointer was NULL the start address of a pointer 
 *              was not 8-byte aligned. 
 *    -    one or more of the entries is equal to 0 in the table referenced 
 *              by pQuantRawTable. 
 *
 */
OMXResult omxICJP_DCTQuantInvTableInit (
    const OMX_U8 *pQuantRawTable,
    OMX_U32 *pQuantInvTable
);



/**
 * Function:  omxICJP_DCTQuantInv_S16   (5.1.3.3.2)
 *
 * Description:
 * Computes an inverse DCT and inverse quantization for 8-bit image data; 
 * processes one block (8x8). 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the input coefficient block (8x8) buffer; must be 
 *            arranged in raster scan order and 8-byte aligned.  Buffer values 
 *            should lie in the range [-2040, 2040]. 
 *   pQuantInvTable - pointer to the quantization table initialized using the 
 *            function DCTQuantInvTableInit. The table contains 64 entries and 
 *            the start address must be 8-byte aligned. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the output pixel block (8x8) buffer; must be 8-byte 
 *            aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -    a pointer was NULL 
 *    -    one of the following pointers was not 8-byte aligned: pSrc, pDst, 
 *              pQuantInvTable. 
 *
 */
OMXResult omxICJP_DCTQuantInv_S16 (
    const OMX_S16 *pSrc,
    OMX_S16 *pDst,
    const OMX_U32 *pQuantInvTable
);



/**
 * Function:  omxICJP_DCTQuantInv_S16_I   (5.1.3.3.3)
 *
 * Description:
 * Computes an inverse DCT and inverse quantization for 8-bit image data; 
 * processes one block (8x8) in-place. 
 *
 * Input Arguments:
 *   
 *   pSrcDst - pointer to the input coefficient block/output pixel block 
 *            buffer (8x8) for in-place processing; must be arranged in raster 
 *            scan order and 8-byte aligned.  Buffer values should lie in the 
 *            range [-2040, 2040]. 
 *   pQuantInvTable - pointer to the quantization table initialized using the 
 *            function DCTQuantInvTableInit. The table contains 64 entries and 
 *            the start address must be 8-byte aligned. 
 *
 * Output Arguments:
 *   
 *   pSrcDst - pointer to the in-place output pixel block(8x8) buffer; must 
 *            be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -    a pointer was NULL 
 *    -    one of the following pointers was not 8-byte aligned: pSrcDst, 
 *              pQuantInvTable. 
 *
 */
OMXResult omxICJP_DCTQuantInv_S16_I (
    OMX_S16 *pSrcDst,
    const OMX_U32 *pQuantInvTable
);



/**
 * Function:  omxICJP_DCTInv_S16   (5.1.3.3.4)
 *
 * Description:
 * This function implements inverse DCT for 8-bit image data.  It processes 
 * one block (8x8). 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the input DCT coefficient block (8x8) buffer; must be 
 *            arranged in raster scan order, and the start address must be 
 *            8-byte aligned.  Buffer values should lie in the range [-2040, 
 *            2040]. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the output image pixel data block(8x8) buffer.  The 
 *            start address must be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: a pointer was NULL the start address of a pointer 
 *              was not 8-byte aligned. 
 *
 */
OMXResult omxICJP_DCTInv_S16 (
    const OMX_S16 *pSrc,
    OMX_S16 *pDst
);



/**
 * Function:  omxICJP_DCTInv_S16_I   (5.1.3.3.5)
 *
 * Description:
 * This function implements an in-place inverse DCT for 8-bit image data.  It 
 * processes one block (8x8). 
 *
 * Input Arguments:
 *   
 *   pSrcDst - pointer to the in-place input DCT coefficient block (8x8) 
 *            buffer and output image pixel data buffer; must be arranged in 
 *            raster scan order and 8-byte aligned.  Buffer values should lie 
 *            in the range [-2040, 2040]. 
 *
 * Output Arguments:
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -    pSrcDst was NULL 
 *    -    pSrcDst was not 8-byte aligned. 
 *
 */
OMXResult omxICJP_DCTInv_S16_I (
    OMX_S16 *pSrcDst
);



/**
 * Function:  omxICJP_DCTQuantInv_Multiple_S16   (5.1.3.3.6)
 *
 * Description:
 * Multiple block dequantization and IDCT function. This function implements 
 * inverse DCT with dequantization for 8-bit image data.  It processes 
 * multiple blocks (each 8x8). The blocks are assumed to be part of a 
 * planarized buffer.  This function should be called separately for luma and 
 * chroma buffers with the respective quantization table.  The start address 
 * of pQuantInvTable must be 8-byte aligned. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the start of the first 8x8 coefficient block in the 
 *            input buffer, which should contain a total of nBlocks x 8 x 8 
 *            coefficients stored contiguously.  The coefficients within each 
 *            block must be arranged in raster scan order, and the start 
 *            address must be 8-byte aligned.  Buffer values should lie in the 
 *            range [-2040, 2040]. 
 *   nBlocks - the number of 8x8 blocks to be processed. 
 *   pQuantInvTable - pointer to the quantization table initialized using the 
 *            function DCTQuantInvTableInit.  The table contains 64 entries 
 *            and the start address must be 8-byte aligned.. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the start of the first 8x8 output pixel block in the 
 *            multi-block output buffer; must be 8-byte aligned.  The buffer 
 *            referenced by pDst must have sufficient free space to contain 
 *            nBlocks blocks of 8x8 pixels stored contiguously. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -   one or more of the following pointers was NULL: pSrc, pDst, or 
 *              pQuantInvTable 
 *    -    one or more of the following pointers was not 8-byte aligned: 
 *              pSrc, pDst, or pQuantInvTable. 
 *
 */
OMXResult omxICJP_DCTQuantInv_Multiple_S16 (
    const OMX_S16 *pSrc,
    OMX_S16 *pDst,
    OMX_INT nBlocks,
    const OMX_U32 *pQuantInvTable
);



/**
 * Function:  omxICJP_EncodeHuffmanSpecGetBufSize_U8   (5.1.3.4.1)
 *
 * Description:
 * Returns the size, in bytes, of the buffer required to store the Huffman 
 * encoder table. 
 *
 * Input Arguments:
 *   
 *   none 
 *
 * Output Arguments:
 *   
 *   pSize - pointer to the size 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr -no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    a pointer was NULL 
 *
 */
OMXResult omxICJP_EncodeHuffmanSpecGetBufSize_U8 (
    OMX_INT *pSize
);



/**
 * Function:  omxICJP_EncodeHuffmanSpecInit_U8   (5.1.3.4.2)
 *
 * Description:
 * Initializes the DC or AC Huffman encoder table specification structure.  
 * The helper function omxICJP_EncodeHuffmanSpecGetBufSize_U8 may be used to 
 * obtain the minimum necessary size, in bytes, of the buffer that will be 
 * required to contain the initialized structure *pHuffTable. 
 *
 * Input Arguments:
 *   
 *   pHuffBits - pointer to the array of HUFFBITS, which contains the number 
 *            of Huffman codes for size 1-16. 
 *   pHuffValue - pointer to the array of HUFFVAL, which contains the symbol 
 *            values to be associated with the Huffman codes ordering by size. 
 *   pHuffTable - pointer to a buffer with sufficient free space to contain 
 *            the initialized OMXICJPHuffmanEncodeSpec output.  The buffer 
 *            size, in bytes, should be equal to the value of the parameter 
 *            *pSize returned by the function 
 *            omxICJP_EncodeHuffmanSpecGetBufSize_U8. Must be aligned on a 
 *            4-byte boundary. 
 *
 * Output Arguments:
 *   
 *   pHuffTable - pointer to an initialized OMXICJPHuffmanEncodeSpec data 
 *            structure; must be aligned on a 4-byte boundary. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -    a pointer was NULL 
 *
 */
OMXResult omxICJP_EncodeHuffmanSpecInit_U8 (
    const OMX_U8 *pHuffBits,
    const OMX_U8 *pHuffValue,
    OMXICJPHuffmanEncodeSpec *pHuffTable
);



/**
 * Function:  omxICJP_EncodeHuffman8x8_Direct_S16_U1_C1   (5.1.3.4.3)
 *
 * Description:
 * Implements the Huffman encoder for baseline mode.  The input raster 
 * scanned source data block (8x8) is zigzag scanned , prior to encoding.  To 
 * match the transposed order of the input DCT, an implicit transpose of the 
 * source data block is integrated into the zigzag scan. The DC prediction 
 * coefficient (*pDCPred) should be initialized to zero and reset to zero 
 * after every restart interval.  Example 5-1 illustrates Huffman encoder 
 * buffer behavior. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source data block (8x8), in transposed order; must 
 *            be aligned on a 32-byte boundary. 
 *   pDCHuffTable - pointer to the OMXICJPHuffmanEncodeSpec data structure 
 *            containing the DC Huffman encoder table; must be aligned on a 
 *            4-byte boundary. 
 *   pACHuffTable - pointer to the OMXICJPHuffmanEncodeSpec data structure 
 *            containing the AC Huffman encoder table; must be aligned on a 
 *            4-byte boundary. 
 *   pSrcDstBitsLen - pointer to the next available bit in the output buffer 
 *            (pDst); informs the Huffman encoder of where to start writing 
 *            the output bits for the current block.  To accommodate a 
 *            non-empty pDst buffer upon function entry, the parameter 
 *            pSrcDstBitsLen indicates the position of the current bit (output 
 *            start position) as an offset relative to pDst, or equivalently, 
 *            the buffer length upon entry in terms of bits.  The number of 
 *            bytes contained in the output buffer is given by 
 *            pSrcDstBitsLen>>3, the number of bits contained in the current 
 *            byte is given by pSrcDstBitsLen&0x7, and the number of bits free 
 *            in the current byte is given by 8 (pSrcDstBitsLen&0x7). There is 
 *            no restriction on buffer length. It is the responsibility of the 
 *            caller to maintain the buffer and limit its length as 
 *            appropriate for the target application or environment. The value 
 *            *pSrcDstBitsLen is updated upon return as described below under 
 *             Output Arguments .  The parameter pSrcDstBitsLen must be 
 *            aligned on a 4-byte boundary. 
 *   pDCPred - pointer to the DC prediction coefficient. Upon input should 
 *            contain the value of the quantized DC coefficient from the most 
 *            recently coded block.  Updated upon return as described below; 
 *            must be aligned on a 4-byte boundary. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the to the first byte in the JPEG output bitstream 
 *            buffer both upon function entry and upon function return, i.e., 
 *            the function does not modify the value of the pointer. The next 
 *            available bit in the buffer is indexed by the parameter 
 *            pSrcDstBitsLen, i.e., for a buffer of non zero length, the value 
 *            of the last bit written during the Huffman block encode 
 *            operation is given by (pDst[currentByte]>>currentBit)&0x1, where 
 *            currentByte=(pSrcDstBitsLen 1)>>3, and 
 *            currentBit=7-((pSrcDstBitsLen-1)&0x7). Within each byte, bits 
 *            are filled from most significant to least significant, and 
 *            buffer contents are formatted in accordance with CCITT T.81. The 
 *            pointer pDst must be aligned on a 4-byte boundary. 
 *   pSrcDstBitsLen - pointer to the next available bit position in the 
 *            output buffer (pDst) following the block Huffman encode 
 *            operation; informs the caller of where the Huffman encoder 
 *            stopped writing bits for the current block. The updated value 
 *            *pSrcDstBitsLen indexes the position following the last bit 
 *            written to the output buffer relative to pDst, or equivalently, 
 *            it indicates the updated number of bits contained in the output 
 *            buffer after block encoding has been completed. Usage guidelines 
 *            apply as described above under  Input Arguments.  
 *   pDCPred - pointer to the DC prediction coefficient. Updated upon return 
 *            to contain the DC coefficient from the current block; the 
 *            pointer must be aligned on a 4-byte boundary. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: a pointer was NULL the start address of a pointer 
 *              was not 4-byte aligned. 
 *    -    *pDstBitsLen was less than 0. 
 *
 */
OMXResult omxICJP_EncodeHuffman8x8_Direct_S16_U1_C1 (
    const OMX_S16 *pSrc,
    OMX_U8 *pDst,
    OMX_INT *pSrcDstBitsLen,
    OMX_S16 *pDCPred,
    const OMXICJPHuffmanEncodeSpec *pDCHuffTable,
    const OMXICJPHuffmanEncodeSpec*pACHuffTable
);



/**
 * Function:  omxICJP_DecodeHuffmanSpecGetBufSize_U8   (5.1.3.5.1)
 *
 * Description:
 * Returns the size, in bytes, of the buffer required to store the Huffman 
 * decoder table. 
 *
 * Input Arguments:
 *   
 *   none 
 *
 * Output Arguments:
 *   
 *   pSize -pointer to the size 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr -no error 
 *    OMX_Sts_BadArgErr -bad arguments 
 *    If following conditions are not satisfied, this function returns 
 *              OMX_Sts_BadArgErr: 
 *    -   pointer cannot be NULL 
 *
 */
OMXResult omxICJP_DecodeHuffmanSpecGetBufSize_U8 (
    OMX_INT *pSize
);



/**
 * Function:  omxICJP_DecodeHuffmanSpecInit_U8   (5.1.3.5.2)
 *
 * Description:
 * Initializes the DC or AC Huffman decoder table specification. 
 *
 * Input Arguments:
 *   
 *   pHuffBits - Pointer to the array of HUFFBITS, which contains the number 
 *            of Huffman codes for size 1-16 
 *   pHuffValue - Pointer to the array of HUFFVAL, which contains the symbol 
 *            values to be associated with the Huffman codes ordering by size 
 *
 * Output Arguments:
 *   
 *   pHuffTable - pointer to a OMXICJPHuffmanDecodeSpec data structure; must 
 *            be aligned on a 4 byte boundary. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments. Returned for any of the following 
 *              conditions: 
 *    -    a pointer was NULL 
 *
 */
OMXResult omxICJP_DecodeHuffmanSpecInit_U8 (
    const OMX_U8 *pHuffBits,
    const OMX_U8 *pHuffValue,
    OMXICJPHuffmanDecodeSpec *pHuffTable
);



/**
 * Function:  omxICJP_DecodeHuffman8x8_Direct_S16_C1   (5.1.3.5.3)
 *
 * Description:
 * Implements the JPEG baseline Huffman decoder. Decodes an 8x8 block of 
 * quantized DCT coefficients using  the tables referenced by the parameters 
 * pDCHuffTable and pACHuffTable in accordance with the Huffman decoding 
 * procedure defined in [ISO10918-1], Annex F.2.2, Baseline Huffman Decoding 
 * Procedures.  If a JPEG marker is detected during decoding, the function 
 * stops decoding and writes the marker to the location indicated by pMarker. 
 * The decoded data is inverse zigzag scanned to produce the raster scanned 
 * output buffer (8x8). The DC coefficient prediction parameter pDCPred should 
 * be set to 0 during initialization and after every restart interval.  The 
 * parameter pMarker should set to 0 during initialization or after the found 
 * marker has been processed.  The parameter pNumValidPrefetchedBits should be 
 * set to 0 in the following cases: 1) during function initialization, 2) 
 * after each restart interval, and 3) after each found marker has been 
 * processed.  The parameter pPrefetchedBits should be set to 0 during 
 * function initialization. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the to the first byte of the input JPEG bitstream 
 *            buffer both upon function entry and upon function return, i.e., 
 *            the function does not modify the value of the pointer.  The 
 *            location of the first available bit in the buffer is indexed by 
 *            the parameter pSrcDstBitsLen. For a buffer of non zero length, 
 *            the value of the first bit accessed during the Huffman block 
 *            deecode operation  is given by 
 *            (pSrc[currentByte]>>currentBit)&0x1, where 
 *            currentByte=(pSrcDstBitsLen 1)>>3, and 
 *            currentBit=7-((pSrcDstBitsLen-1)&0x7).  Within each byte, bits 
 *            are consumed from most significant to least significant. The 
 *            buffer contents should be formatted in accordance with CCITT 
 *            T.81, and the pointer pSrc must be aligned on a 4-byte boundary. 
 *   pDCHuffTable - pointer to the OMXICJPHuffmanDecodeSpec structure 
 *            containing the DC Huffman decoding table; must be aligned on a 
 *            4-byte boundary. 
 *   pACHuffTable - pointer to the OMXICJPHuffmanDecodeSpec structure 
 *            containing the AC Huffman decoding table; must be aligned on a 
 *            4-byte aligned. 
 *   pSrcDstBitsLen - pointer to the current bit position indicator for the 
 *            input buffer (pSrc). This parameter informs the Huffman decoder 
 *            of where to start reading input bits for the current block since 
 *            the start of the current block may not necessarily be positioned 
 *            at the start of the input buffer.  The parameter pSrcDstBitsLen 
 *            indicates the offset in terms of bits of the current bit 
 *            relative to pSrc. Updated upon return as described below under 
 *             Output Arguments .  There is no restriction on buffer length.  
 *            It is the responsibility of the caller to maintain the Huffman 
 *            buffer and limit the buffer length as appropriate for the target 
 *            application or environment.  The parameter pSrcDstBitsLen must 
 *            be aligned on a 4-byte boundary. 
 *   pDCPred - pointer to the DC prediction coefficient. Upon input contains 
 *            the quantized DC coefficient decoded from the most recent block. 
 *             Should be set to 0 upon function initialization and after each 
 *            restart interval.  Updated upon return as described below under 
 *             Output Arguments.  
 *   pMarker - pointer to the most recently encountered marker.  The caller 
 *            should set this parameter to 0 during function initialization 
 *            and after a found marker has been processed.  Updated upon 
 *            return as described below under  Output Arguments.  
 *   pPrefetchedBits - implementation-specific pre-fetch parameter; should be 
 *            set to 0 during function initialization. This parameter shall 
 *            not affect the value of the parameter pSrcDstBitsLen, i.e., the 
 *            location of the next bit in the stream shall be fully determined 
 *            by combining the parameters pSrc and pSrcDstBitsLen as described 
 *            above under the description for the parameter pSrc. 
 *   pNumValidPrefetchedBits - implementation-specific pre-fetch parameter 
 *            that points to the number of valid bits in the pre-fetch buffer; 
 *            should be set to 0 upon input under the following conditions:  
 *            1) function initialization, 2) after each restart interval, 3) 
 *            after each found marker has been processed.  This parameter 
 *            shall not affect the value of the parameter pSrcDstBitsLen, 
 *            i.e., the location of the next bit in the stream shall be fully 
 *            determined by combining the parameters pSrc and pSrcDstBitsLen 
 *            as described above under the description for the parameter pSrc. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the output buffer; must be aligned on a 32-byte 
 *            boundary. 
 *   pSrcDstBitsLen - pointer to the updated value of the bit index for the 
 *            input buffer (pSrc); informs the caller of where the Huffman 
 *            decoder stopped reading bits for the current block.  The value 
 *            *pSrcDstBitsLen is modified by the Huffman decoder such that it 
 *            indicates upon return  the offset in terms of bits of the 
 *            current bit relative to pSrc after block decoding has been 
 *            completed.  Usage guidelines apply as described above under 
 *             Input Arguments.  
 *   pDCPred - pointer to the DC prediction coefficient.  Returns the 
 *            quantized value of the DC coefficient from the current block. 
 *   pMarker - pointer to the most recently encountered marker.  If a marker 
 *            is detected during decoding, the function stops decoding and 
 *            returns the encountered marker using this parameter; returned 
 *            value should be preserved between calls to the decoder or reset 
 *            prior to input as described above under  Input Arguments.  
 *   pPrefetchedBits - implementation-specific pre-fetch parameter; returned 
 *            value should be preserved between calls to the decoder or reset 
 *            prior to input as described above under  Input Arguments.  
 *   pNumValidPrefetchedBits - pointer to the number of valid bits in the 
 *            pre-fetch buffer; returned value should be preserved between 
 *            calls to the decoder or reset prior to input as described above 
 *            under  Input Arguments.  
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error. 
 *    OMX_Sts_Err - error, illegal Huffman code encountered in the input 
 *              bitstream. 
 *    OMX_StsICJP_JPEGMarkerErr - JPEG marker encountered within an 
 *              entropy-coded block; Huffman decoding terminated early.  
 *              Marker value returned by the parameter *pMarker. 
 *    OMX_StsICJP_JPEGMarker - JPEG marker encountered before any Huffman 
 *              symbols; Huffman decoding terminated early.  Marker value 
 *              returned by the parameter *pMarker. 
 *    OMX_Sts_BadArgErr - bad arguments; returned under any of the following 
 *              conditions: 
 *    -    a pointer was NULL 
 *    -    *pSrcBitsLen was less than 0. 
 *    -    *pNumValidPrefetchedBits was less than 0. 
 *    -    the start address of pDst was not 32-byte aligned. 
 *
 */
OMXResult omxICJP_DecodeHuffman8x8_Direct_S16_C1 (
    const OMX_U8 *pSrc,
    OMX_INT *pSrcDstBitsLen,
    OMX_S16 *pDst,
    OMX_S16 *pDCPred,
    OMX_INT *pMarker,
    OMX_U32 *pPrefetchedBits,
    OMX_INT *pNumValidPrefetchedBits,
    const OMXICJPHuffmanDecodeSpec *pDCHuffTable,
    const OMXICJPHuffmanDecodeSpec *pACHuffTable
);



#ifdef __cplusplus
}
#endif

#endif /** end of #define _OMXIC_H_ */

/** EOF */

