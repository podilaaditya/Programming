/**
 * File: omxIP.h
 * Brief: OpenMAX DL v1.0.2 - Image Processing library
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

#ifndef _OMXIP_H_
#define _OMXIP_H_

#include "omxtypes.h"

#ifdef __cplusplus
extern "C" {
#endif


/* 4.1.5.2 Vendor-Specific Moment States Data Structure */
 typedef void OMXMomentState;


/* 4.1.5.1 Rotations  */
/* OMXIPRotation, a data type that enumerates image rotations, is defined as follows:  */

typedef enum {
    OMX_IP_DISABLE = 0,
    OMX_IP_ROTATE90L = 1,       /* counter-clockwise */
    OMX_IP_ROTATE90R = 2,       /* clockwise */
    OMX_IP_ROTATE180 = 3,
    OMX_IP_FLIP_HORIZONTAL = 4, /* from R to L, about V axis */
    OMX_IP_FLIP_VERTICAL = 5    /* from top to bottom, about H axis */
} OMXIPRotation;



/**
 * Function:  omxIPBM_Copy_U8_C1R   (4.2.1.1.2)
 *
 * Description:
 * Copy pixel values from the ROI of the source image referenced by the 
 * pointer pSrc to the ROI of the destination image referenced by the pointer 
 * pDst. The source ROI should not overlap the destination ROI within the same 
 * image buffer. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -   pSrc or pDst is NULL 
 *    -   srcStep or dstStep is less than or equal to zero 
 *    -   roiSize has a field with zero or negative value. 
 *
 */
OMXResult omxIPBM_Copy_U8_C1R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize
);



/**
 * Function:  omxIPBM_Copy_U8_C3R   (4.2.1.1.2)
 *
 * Description:
 * Copy pixel values from the ROI of the source image referenced by the 
 * pointer pSrc to the ROI of the destination image referenced by the pointer 
 * pDst. The source ROI should not overlap the destination ROI within the same 
 * image buffer. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -   pSrc or pDst is NULL 
 *    -   srcStep or dstStep is less than or equal to zero 
 *    -   roiSize has a field with zero or negative value. 
 *
 */
OMXResult omxIPBM_Copy_U8_C3R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize
);



/**
 * Function:  omxIPBM_AddC_U8_C1R_Sfs   (4.2.1.2.2)
 *
 * Description:
 * Computes corresponding arithmetic operation with a constant and each 
 * element of the source ROI and places the scaled result in the destination 
 * image referenced by the pointer pDst. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   value - constant for operation 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *   scaleFactor - scale factor value, valid in the range [-7,7] 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pDst is NULL 
 *    -    srcStep or dstStep is less than or equal to zero 
 *    -    roiSize has a field with zero or negative value. 
 *    -    scaleFactor is outside the range [-7,7] 
 *
 */
OMXResult omxIPBM_AddC_U8_C1R_Sfs (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 value,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize,
    OMX_INT scaleFactor
);



/**
 * Function:  omxIPBM_MulC_U8_C1R_Sfs   (4.2.1.2.2)
 *
 * Description:
 * Computes corresponding arithmetic operation with a constant and each 
 * element of the source ROI and places the scaled result in the destination 
 * image referenced by the pointer pDst. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   value - constant for operation 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *   scaleFactor - scale factor value, valid in the range [-7,7] 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pDst is NULL 
 *    -    srcStep or dstStep is less than or equal to zero 
 *    -    roiSize has a field with zero or negative value. 
 *    -    scaleFactor is outside the range [-7,7] 
 *
 */
OMXResult omxIPBM_MulC_U8_C1R_Sfs (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 value,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize,
    OMX_INT scaleFactor
);



/**
 * Function:  omxIPBM_Mirror_U8_C1R   (4.2.1.3.1)
 *
 * Description:
 * This function mirrors the source image pSrc about a horizontal or vertical 
 * axis or both, depending on the value of the parameter axis, and writes it 
 * to the destination image pDst. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source buffer 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels. 
 *   axis - specifies the axis about which to mirror the image.  Must use one 
 *            of the following OMXIPRotation values: 
 *              - OMX_IP_FLIP_HORIZONTAL to mirror about the vertical axis 
 *              - OMX_IP_FLIP_VERTICAL to mirror about the horizontal axis 
 *              - OMX_IP_ROTATE180 to mirror about both horizontal and vertical axes 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination buffer 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -   pSrc or pDst is NULL 
 *    -   srcStep or dstStep is less than or equal to zero 
 *    -   roiSize has a field with zero or negative value. 
 *    -   axis contains an illegal value 
 *
 */
OMXResult omxIPBM_Mirror_U8_C1R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize,
    OMXIPRotation axis
);



/**
 * Function:  omxIPPP_FilterFIR_U8_C1R   (4.3.1.1.1)
 *
 * Description:
 * Performs filtering of the ROI of the source image pointed to by pSrc using 
 * a general rectangular (WxH size) convolution kernel. The value of the 
 * output pixel is normalized by the divider and saturated as:
 *
 *                SAT<U8>(1/divider  *  PSI<2D>(h,a,x)) 
 * 
 * The result is placed into the ROI of the destination image pointed to by pDst. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *   pKernel - pointer to the 2D FIR filter coefficients 
 *   kernelSize - size of the FIR filter kernel.  The minimum valid size is 
 *            1x1.  There is no limit on the maximum size other than the 
 *            practical limitation imposed by the ROI size and location 
 *            relative to the image boundaries.  The caller should avoid 
 *            kernel overlap with invalid buffer locations given ROI size, ROI 
 *            placement relative to the image buffer boundaries, and the FIR 
 *            operator definition given in Table 4-4. 
 *   anchor - anchor cell specifying the alignment of the array of filter 
 *            taps with respect to the position of the input pixel 
 *   divider - value of the divider used to normalize the result 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pDst is NULL 
 *    -    srcStep or dstStep is less than or equal to zero 
 *    -    roiSize has a field with zero or negative value. 
 *    -    anchor specifies a point outside of the mask divider is out of range 
 *
 */
OMXResult omxIPPP_FilterFIR_U8_C1R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize,
    const OMX_S32 *pKernel,
    OMXSize kernelSize,
    OMXPoint anchor,
    OMX_INT divider
);



/**
 * Function:  omxIPPP_FilterMedian_U8_C1R   (4.3.1.1.2)
 *
 * Description:
 * Performs median filtering of the ROI of the source image pointed to by 
 * pSrc using the median filter of the size maskSize and location anchor, and 
 * places the result into the ROI of the destination image pointed to by pDst. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI, in pixels 
 *   maskSize - size of the mask, in pixels; minimum size is 3x3; maximum 
 *            size is 31x31. 
 *   anchor - anchor cell specifying the mask alignment with respect to the 
 *            position of the input pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -   pSrc or pDst is NULL 
 *    -   srcStep or dstStep is less than or equal to zero 
 *    -   roiSize has a field with zero or negative value. 
 *    -   anchor specifies a point outside of the mask 
 *    -   maskSize is out of range, i.e., smaller than 3x3 or larger than 31x31
 *
 */
OMXResult omxIPPP_FilterMedian_U8_C1R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize,
    OMXSize maskSize,
    OMXPoint anchor
);



/**
 * Function:  omxIPPP_MomentGetStateSize   (4.3.1.2.1)
 *
 * Description:
 * Get size of state structure in bytes; returned in *pSize. 
 *
 * Output Arguments:
 *   
 *   pSize - pointer to the size of structure 
 *
 * Return Value:
 *
 * OMX_Sts_NoErr - no errors detected 
 * OMX_Sts_BadArgErr -  bad arguments detected; at least one of the 
 *                following is true:  
 * -   pSize is NULL  
 * 
 */
OMXResult omxIPPP_MomentGetStateSize (
    OMX_INT *pSize
);
 


 /**
 * Function:  omxIPPP_MomentInit   (4.3.1.2.2)
 *
 * Description:
 * Initialize moment state structure. 
 *
 * Input Arguments:
 *   
 *   pState - pointer to the uninitialized state structure 
 *
 * Output Arguments:
 *   
 *   pState - pointer to the initialized state structure 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -   pState is NULL 
 *
 */
OMXResult omxIPPP_MomentInit(
    OMXMomentState *pState
); 



/**
 * Function:  omxIPPP_Moments_U8_C1R   (4.3.1.2.4)
 *
 * Description:
 * Computes statistical spatial moments of order 0 to 3 for the ROI of the 
 * image pointed to by pSrc. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   roiSize - size of the ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pState - pointer to the state structure 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pState is NULL 
 *    -    srcStep is less than or equal to zero 
 *    -    roiSize has a field with zero or negative value. 
 *    OMX_StsIPPP_ContextMatchErr - contents of the implementation-specific 
 *              structure OMXMomentState are invalid 
 *
 */
OMXResult omxIPPP_Moments_U8_C1R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMXSize roiSize,
    OMXMomentState *pState
);



/**
 * Function:  omxIPPP_Moments_U8_C3R   (4.3.1.2.4)
 *
 * Description:
 * Computes statistical spatial moments of order 0 to 3 for the ROI of the 
 * image pointed to by pSrc. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   roiSize - size of the ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pState - pointer to the state structure 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors detected 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pState is NULL 
 *    -    srcStep is less than or equal to zero 
 *    -    roiSize has a field with zero or negative value. 
 *    OMX_StsIPPP_ContextMatchErr - contents of the implementation-specific 
 *              structure OMXMomentState are invalid 
 *
 */
OMXResult omxIPPP_Moments_U8_C3R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMXSize roiSize,
    OMXMomentState *pState
);



/**
 * Function:  omxIPPP_GetSpatialMoment_S64   (4.3.1.2.5)
 *
 * Description:
 * Returns nOrd by mOrd spatial moment calculated by the Moments_U8 function. 
 * Places the scaled result into the memory pointed to by pValue. 
 *
 * Input Arguments:
 *   
 *   nOrd, mOrd - moment specifiers 
 *   pState - pointer to the state structure 
 *   nChannel - specifies the desired image channel from which to extract the 
 *            spatial moment.  For a C3 input image, the valid range is from 
 *            0-2. For a C1 input image, the only valid value is 0. 
 *   roiOffset - offset in pixels of the ROI origin (top left corner) from 
 *            the image origin 
 *   scaleFactor - value of the scale factor 
 *
 * Output Arguments:
 *   
 *   pValue -pointer to the computed moment value 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pState or pValue is NULL 
 *    -    nChannel contains an invalid channel number 
 *    OMX_StsIPPP_ContextMatchErr - contents of the implementation-specific 
 *              structure OMXMomentState are invalid 
 *
 */
OMXResult omxIPPP_GetSpatialMoment_S64 (
    const OMXMomentState*pState,
    OMX_INT mOrd,
    OMX_INT nOrd,
    OMX_INT nChannel,
    OMXPoint roiOffset,
    OMX_S64 *pValue,
    OMX_INT scaleFactor
);



/**
 * Function:  omxIPPP_GetCentralMoment_S64   (4.3.1.2.6)
 *
 * Description:
 * Returns the nOrd by mOrd central moment calculated by the Moments_U8 
 * function, and places the scaled result into the memory pointed to by 
 * pValue. 
 *
 * Input Arguments:
 *   
 *   pState - pointer to the state structure 
 *   mOrd,nOrd - specify the required spatial moment 
 *   nChannel - specifies the desired image channel from which to extract the 
 *            spatial moment.  For a C3 input image, the valid range is from 
 *            0-2. For a C1 input image, the only valid value is 0. 
 *   scaleFactor - value of the scale factor 
 *
 * Output Arguments:
 *   
 *   pValue - pointer to the computed moment value 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pState or pValue is NULL 
 *    -    nChannel contains an invalid channel number 
 *    OMX_StsIPPP_ContextMatchErr - contents of the implementation-specific 
 *              structure OMXMomentState are invalid 
 *
 */
OMXResult omxIPPP_GetCentralMoment_S64 (
    const OMXMomentState *pState,
    OMX_INT mOrd,
    OMX_INT nOrd,
    OMX_INT nChannel,
    OMX_S64 *pValue,
    OMX_INT scaleFactor
);



/**
 * Function:  omxIPPP_Deblock_HorEdge_U8_I   (4.3.1.3.2)
 *
 * Description:
 * Performs deblock filtering for a single 8x8 macroblock along a block edge 
 * (horizontal-top or vertical-left), as shown in the figure (see OpenMAX DL
 * specification, v1.0.2). 
 * Block edges are represented in the figure by heavy lines.  The horizontal 
 * edge deblocking function processes the top edge of the block referenced by 
 * pSrcDst. The vertical edge deblocking function processes the left edge of 
 * the block referenced by pSrcDst. For each processed column , the horizontal 
 * edge deblocking operation modifies two pixels from the source block (pixels 
 * C,D) and two pixels in the neighboring block (pixels A,B).  For each 
 * processed row, the vertical edge deblocking operation modifies two pixels 
 * from the source block (pixels C,D) and two pixels from the neighboring 
 * block (A,B). 
 *
 * Input Arguments:
 *   
 *   pSrcDst - pointer to the first pixel of the second block (labeled  block 
 *            2  in the figure); must be aligned on an 8-byte boundary. 
 *   step - distance, in bytes; between start of each line; must be a 
 *            multiple of 8 
 *   QP - quantization parameter, as described in Section J.3 of Annex J in 
 *            H.263+; valid in the range 1 to 31. 
 *
 * Output Arguments:
 *   
 *   pSrcDst - pointer to the first pixel of the second output block (labeled 
 *             block 2  in the figure below); must be aligned on an 8-byte 
 *            boundary. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments:
 *    -    pSrcDst is NULL or not aligned on an 8-byte boundary 
 *    -    QP exceeds [1,31] 
 *    -    step is not a multiple of 8 or step is less than 8 
 *
 */
OMXResult omxIPPP_Deblock_HorEdge_U8_I (
    OMX_U8 *pSrcDst,
    OMX_INT step,
    OMX_INT QP
);



/**
 * Function:  omxIPPP_Deblock_VerEdge_U8_I   (4.3.1.3.2)
 *
 * Description:
 * Performs deblock filtering for a single 8x8 macroblock along a block edge 
 * (horizontal-top or vertical-left), as shown in the figure (see OpenMAX DL
 * specification, v1.0.2). 
 * Block edges are represented in the figure by heavy lines.  The horizontal 
 * edge deblocking function processes the top edge of the block referenced by 
 * pSrcDst. The vertical edge deblocking function processes the left edge of 
 * the block referenced by pSrcDst. For each processed column , the horizontal 
 * edge deblocking operation modifies two pixels from the source block (pixels 
 * C,D) and two pixels in the neighboring block (pixels A,B).  For each 
 * processed row, the vertical edge deblocking operation modifies two pixels 
 * from the source block (pixels C,D) and two pixels from the neighboring 
 * block (A,B). 
 *
 * Input Arguments:
 *   
 *   pSrcDst - pointer to the first pixel of the second block (labeled  block 
 *            2  in the figure); must be aligned on an 8-byte boundary. 
 *   step - distance, in bytes; between start of each line; must be a 
 *            multiple of 8 
 *   QP - quantization parameter, as described in Section J.3 of Annex J in 
 *            H.263+; valid in the range 1 to 31. 
 *
 * Output Arguments:
 *   
 *   pSrcDst - pointer to the first pixel of the second output block (labeled 
 *             block 2  in the figure below); must be aligned on an 8-byte 
 *            boundary. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    pSrcDst is NULL or not aligned on an 8-byte boundary 
 *    -    QP exceeds [1,31] 
 *    -    step is not a multiple of 8 or step is less than 8 
 *
 */
OMXResult omxIPPP_Deblock_VerEdge_U8_I (
    OMX_U8 *pSrcDst,
    OMX_INT step,
    OMX_INT QP
);



/* 4.4.2.1 Interpolation Schemes  */
/* OMXIPInterpolation, a data type that enumerates image interpolation schemes, is defined as follows:  */

typedef enum {
    OMX_IP_NEAREST = 0,
    OMX_IP_BILINEAR = 1,
    OMX_IP_MEDIAN = 2 
} OMXIPInterpolation;



/* 4.4.2.2 Color Spaces  */
/* OMXIPColorSpace, a data type that enumerates color spaces, is defined as follows:  */

typedef enum {
    OMX_IP_BGR565 = 0,
    OMX_IP_BGR555 = 1,
    OMX_IP_BGR444 = 2,
    OMX_IP_BGR888 = 3,
    OMX_IP_YCBCR422 = 4,
    OMX_IP_YCBCR420 = 5
} OMXIPColorSpace;



/**
 * Function:  omxIPCS_YCbCr444ToBGR888_U8_C3R   (4.4.3.1.2)
 *
 * Description:
 * Converts a pixel-oriented YCbCr444 image to BGR888 or BGR565 color space.  
 * The ROI of the source image is pointed to by pSrc, and the result is placed 
 * into the ROI of the destination image pointed to by pDst. The input and 
 * output images are organized, respectively, as specified by the Table 4-1 
 * entries labeled  YCbCr444  and  BGR888 / BGR565.  
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -   pSrc or pDst is NULL 
 *    -   srcStep/3 < roiSize.width 
 *    -   omxIPCS_YCbCr444ToBGR888_U8_C3R() only: dstStep/3 < roiSize.width 
 *    -   omxIPCS_YCbCr444ToBGR565_U8_U16_C3R() only: dstStep/2 < roiSize.width 
 *    -   roiSize has a field with zero or negative value 
 *
 */
OMXResult omxIPCS_YCbCr444ToBGR888_U8_C3R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize
);



/**
 * Function:  omxIPCS_YCbCr444ToBGR565_U8_U16_C3R   (4.4.3.1.2)
 *
 * Description:
 * Converts a pixel-oriented YCbCr444 image to BGR888 or BGR565 color space.  
 * The ROI of the source image is pointed to by pSrc, and the result is placed 
 * into the ROI of the destination image pointed to by pDst. The input and 
 * output images are organized, respectively, as specified by the Table 4-1 
 * entries labeled  YCbCr444  and  BGR888 / BGR565.  
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -   pSrc or pDst is NULL srcStep/3 < roiSize.width 
 *    -   omxIPCS_YCbCr444ToBGR888_U8_C3R() only: dstStep/3 < roiSize.width 
 *    -   omxIPCS_YCbCr444ToBGR565_U8_U16_C3R() only: dstStep/2 < roiSize.width 
 *    -   roiSize has a field with zero or negative value 
 *
 */
OMXResult omxIPCS_YCbCr444ToBGR565_U8_U16_C3R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U16 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize
);



/**
 * Function:  omxIPCS_YCbCr444ToBGR565_U8_U16_P3C3R   (4.4.3.1.3)
 *
 * Description:
 * Converts a planar YCbCr444 input image to a pixel-oriented BGR565 output 
 * image.  The ROI of the source image is pointed to by pSrc, and the result 
 * is placed into the ROI of the destination image pointed to by pDst. The 
 * YCbCr444 input image is organized in memory as specified in Table 4-2.  The 
 * pixel-oriented BGR565 output image is organized in memory as specified in 
 * Table 4-1. 
 *
 * Input Arguments:
 *   
 *   pSrc - vector containing pointers to Y, Cb, and Cr planes 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination buffer 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr -No error. 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -   pSrc or pDst is NULL srcStep < roiSize.width 
 *    -   dstStep/2 < roiSize.width 
 *    -   roiSize has a field with zero or negative value 
 *
 */
OMXResult omxIPCS_YCbCr444ToBGR565_U8_U16_P3C3R (
    const OMX_U8 *pSrc[3],
    OMX_INT srcStep,
    OMX_U16 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize
);



/**
 * Function:  omxIPCS_YCbCr420ToBGR565_U8_U16_P3C3R   (4.4.3.1.4)
 *
 * Description:
 * This function converts a planar YCbCr420 input image to a pixel-oriented 
 * BGR565 output image.  The memory organization for a planar YCbCr420 image 
 * is specified in Table 4-2.  The memory organization for a pixel-oriented 
 * BGR565 image is specified in Table 4-1. 
 *
 * Input Arguments:
 *   
 *   pSrc - an array of pointers to the YCbCr420 source planes 
 *   srcStep - an array of three step values; which represent for each image 
 *            plane, respectively, the distance in bytes between the starts of 
 *            consecutive lines in the source image.  The following must be 
 *            true:  srcStep[0] >= roiSize.width, srcStep[1]*2 >= 
 *            roiSize.width, and srcStep[2]*2 >= roiSize.width. 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image, must be an even number, and the following 
 *            condition must be true: dstStep/2 >= roiSize.width. 
 *   roiSize - size of the source and destination ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination buffer; BGR565 is represented using 
 *            16-bit words that are orgranized as specified in Table 4-1. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - No error. Any other value indicates an error or a 
 *              warning 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pDst is NULL 
 *    -    dstStep has a non-even value 
 *    -    at least one of the values srcStep[0..2] or dstStep[0..2] is negative 
 *    -    srcStep[0] < roiSize.width 
 *    -    srcStep[1]*2 < roiSize.width 
 *    -    srcStep[2]*2 < roiSize.width 
 *    -    dstStep/2 < roiSize.width 
 *
 */
OMXResult omxIPCS_YCbCr420ToBGR565_U8_U16_P3C3R (
    const OMX_U8 *pSrc[3],
    OMX_INT srcStep[3],
    OMX_U16 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize
);



/**
 * Function:  omxIPCS_YCbYCr422ToBGR888_U8_C2C3R   (4.4.3.1.6)
 *
 * Description:
 * Convert a pixel-oriented YCbYCr422 input image to a pixel-oriented BGR888 
 * or BGR565 output image.  The ROI of the source image is pointed to by pSrc, 
 * and the result is placed into the ROI of the destination image referenced 
 * by pDst. Memory organization for pixel-oriented YCbYCr422, BGR888, and 
 * BGR565 images is specified in Table 4-1. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pDst is NULL 
 *    -    srcStep or dstStep is less than or equal to zero 
 *    -    srcStep/2 < roiSize.width 
 *    -    omxIPCS_YCbYCr422ToBGR888_U8_C2C3R() only: dstStep/3 < roiSize.width 
 *    -    omxIPCS_YCbYCr422ToBGR565_U8_U16_C2C3R() only: dstStep/2 < roiSize.width 
 *    -    roiSize has a field with zero or negative value 
 *
 */
OMXResult omxIPCS_YCbYCr422ToBGR888_U8_C2C3R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize
);



/**
 * Function:  omxIPCS_YCbYCr422ToBGR565_U8_U16_C2C3R   (4.4.3.1.6)
 *
 * Description:
 * Convert a pixel-oriented YCbYCr422 input image to a pixel-oriented BGR888 
 * or BGR565 output image.  The ROI of the source image is pointed to by pSrc, 
 * and the result is placed into the ROI of the destination image referenced 
 * by pDst. Memory organization for pixel-oriented YCbYCr422, BGR888, and 
 * BGR565 images is specified in Table 4-1. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pDst is NULL 
 *    -    srcStep or dstStep is less than or equal to zero 
 *    -    srcStep/2 < roiSize.width 
 *    -    omxIPCS_YCbYCr422ToBGR888_U8_C2C3R() only: dstStep/3 < roiSize.width 
 *    -    omxIPCS_YCbYCr422ToBGR565_U8_U16_C2C3R() only: dstStep/2 < roiSize.width 
 *    -    roiSize has a field with zero or negative value 
 *
 */
OMXResult omxIPCS_YCbYCr422ToBGR565_U8_U16_C2C3R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U16 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize
);



/**
 * Function:  omxIPCS_ColorTwistQ14_U8_C3R   (4.4.3.2.1)
 *
 * Description:
 * Applies a Q17.14 color twist matrix to the ROI of the source image pointed 
 * to by pSrc. Places the results into the ROI of the destination image 
 * pointed to by pDst. The Q14 modifier with a parameter of type OMX_S32 is 
 * used to indicate the fact that the matrix entries are obtained by 
 * multiplying the entries of the equivalent floating-point color twist matrix 
 * with (1<<14). 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source ROI; should contain three interleaved 
 *            ( C3 ) channels of 8-bit image data. 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image 
 *   roiSize - size of the source and destination ROI in pixels 
 *   twistQ14 - twist matrix 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the destination ROI; contains transformed version of 
 *            the input ROI. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments detected; at least one of the 
 *              following is true: 
 *    -    pSrc or pDst is NULL 
 *    -    srcStep or dstStep is less than or equal to zero 
 *    -    srcStep/3 < roiSize.width 
 *    -    dstStep/3 < roiSize.width 
 *    -    roiSize has a field with zero or negative value 
 *
 */
OMXResult omxIPCS_ColorTwistQ14_U8_C3R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize,
    const OMX_S32 twistQ14[3][4]
);



/**
 * Function:  omxIPCS_CbYCrY422RszCscRotBGR_U8_U16_C2R   (4.4.3.3.1)
 *
 * Description:
 * This function synthesizes a low-resolution preview image from the input 
 * image.  In particular, the following sequence of operations is applied to 
 * the input image: 
 *
 *   1. Scale reduction by an integer scalefactor.  First, the input image
 *   scale is reduced by an integer scalefactor of either 2, 4, or 8 
 *   on both axes using the interpolation methodology specified by the  
 *   control parameter interpolation. The following interpolation schemes 
 *   are supported: nearest neighbor, bilinear. 
 *
 *   2. Color space conversion.  Following scale reduction, color
 *   space conversion is applied from the CbYCrY422 input color 
 *   space to a particular BGR target space determined by the control parameter 
 *   RGBSpec. 
 *
 *   3. Rotation. After color space conversion, the preview output 
 *   image is rotated according to the control parameter rotation. 
 *
 * The start address of pSrc, pDst, must be aligned at 8-byte 
 * boundary; srcStep and dstStep must be multiple of 8. Size of 
 *
 * If roiSize.width or roiSize.height cannot be divided by scaleFactor 
 * exactly, it will be cut to be the multiple of scaleFactor. For example, 
 * if the rotation control parameter is OMX_IP_DISABLE or OMX_IP_ROTATE180, 
 * the output image's roiSize.width is equal to:
 *
 *     ROUND((input image's roiSize.width)/scaleFactor)
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the start of the buffer containing the pixel-oriented 
 *            input image 
 *   srcStep - distance, in bytes, between the start of lines in the source 
 *            image 
 *   dstStep - distance, in bytes, between the start of lines in the 
 *            destination image 
 *   roiSize - dimensions, in pixels, of the source region of interest 
 *   scaleFactor - reduction scalefactor; values other than 2, 4, or 8 are 
 *            invalid 
 *   interpolation - interpolation methodology control parameter; must take 
 *            one of the following values: OMX_IP_NEAREST or OMX_IP_BILINEAR 
 *            for nearest neighbor or bilinear interpolation, respectively 
 *   RGBSpec - color conversion control parameter; must be set to one of the 
 *            following pre-defined values: OMX_IP_BGR565 or OMX_IP_BGR555 
 *   rotation - rotation control parameter; must be set to one of the 
 *            following pre-defined values: OMX_IP_DISABLE, OMX_IP_ROTATE90L, 
 *            OMX_IP_ROTATE90R, or OMX_IP_ROTATE180 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the start of the buffer containing the resized, 
 *            color-converted, and rotated output image 
 *
 * Return Value:
 *    If the function runs without error, it returns OMX_Sts_NoErr 
 *    If one of the following occurs, the function returns OMX_Sts_BadArgErr: 
 *    -   pSrc or pDst is NULL 
 *    -   pSrc or pDst is not aligned on an 8-byte boundary 
 *    -   srcStep or dstStep is less than 1, 
 *    -   srcStep, or dstStep is not a multiple of 8 
 *    -   roiSize.width is larger than half of srcStep 
 *    -   roiSize.width or roiSize.height is less than scaleFactor 
 *    Invalid values of one or more of the following control parameters: 
 *    -    scaleFactor, interpolation, colorConversion or rotation 
 *    -    Half of the dstStep is less than width of downscaled, 
 *              color-converted and rotated image 
 *
 */
OMXResult omxIPCS_CbYCrY422RszCscRotBGR_U8_U16_C2R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U16 *pDst,
    OMX_INT dstStep,
    OMXSize roiSize,
    OMX_INT scaleFactor,
    OMXIPInterpolation interpolation,
    OMXIPColorSpace RGBSpec,
    OMXIPRotation rotation
);



/**
 * Function:  omxIPCS_YCbCr420RszRot_U8_P3R   (4.4.3.4.2)
 *
 * Description:
 * This function combines two atomic image processing kernels into a single 
 * function. The following sequence of operations is applied: 
 *  
 *   1. Resize. The input image of dimension srcSize is rescaled according 
 *   to the Q16 reciprocal ratio scaling control parameters rcpRatiox
 *   and rcpRatioy using the interpolation/decimation methodology specified 
 *   by the control parameter interpolation. Nearest neighbor and bilinear 
 *   interpolation schemes are supported.  The rescaled image is clipped to 
 *   dstSize using a clipping rectangle, the origin of which coincides with 
 *   the top, left corner of the input image, i.e., pixels are discarded 
 *   along the right and bottom edges. 
 *
 *   2. Rotation. The output image is rotated with respect to the input 
 *   image according to the control parameter rotation. 
 * 
 * The input data should be YCbCr420 planar format for <omxIPCS_YCbCr420RszRot_U8_P3R> 
 * and YCbCr422 planar format for <omxIPCS_YCbCr422RszRot_U8_P3R>. 
 *
 * Input Arguments:
 *   
 *   pSrc - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr420 input planes for <omxIPCS_YCbCr420RszRot_U8_P3R> 
 *            and YCbCr422 input planes for <omxIPCS_YCbCr422RszRot_U8_P3R>. 
 *            The pointer pSrc[0] must be aligned on a 4-byte boundary. 
 *   srcStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the input image planes; the 
 *            parameter srcStep[0] must be a multiple of 4. 
 *   dstStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the output image planes; the 
 *            parameter dstStep[0] must be a multiple of 4. 
 *   srcSize - dimensions, in pixels, of the source image 
 *   dstSize - dimensions, in pixels, of the destination image (before 
 *            applying rotation to the resized image).  The parameters 
 *            dstSize.width and dstSize.height must be even. 
 *   interpolation - interpolation methodology control parameter; must take 
 *            one of the following values: OMX_IP_NEAREST, or OMX_IP_BILINEAR 
 *            for nearest neighbor or bilinear interpolation, respectively 
 *   rotation - rotation control parameter; must be set to one of the 
 *            following pre-defined values: . OMX_IP_DISABLE, 
 *            OMX_IP_ROTATE90L, OMX_IP_ROTATE90R, OMX_IP_ROTATE180, 
 *            OMX_IP_FLIP_HORIZONTAL, or OMX_IP_FLIP_VERTICAL 
 *   rcpRatiox - x direction scaling control parameter, specified in terms of 
 *            a reciprocal resize ratio using a Q16 representation. Valid in 
 *            the range [1, xrr_max], where xrr_max = (OMX_INT) ((( (OMX_F32) 
 *            ((srcSize.width&~1)-1) / ((dstSize.width&~1)-1)) * (1<<16) 
 *            +0.5). Setting rcpRatiox = xrr_max guarantees that the output 
 *            image size will be exactly dstSize; otherwise for values less 
 *            than xrr_max the right hand side of the image will be clipped 
 *            since the output image will extend beyond dstSize.  Expansion in 
 *            the x direction occurs for values of rcpRatiox < 65536; 
 *            contraction in the x direction occurs for values > 65536.  To 
 *            avoid clipping, use the value xrr_max, which will ensure that 
 *            the output image width == dstSize.width.  Values larger than 
 *            xrr_max are invalid, i.e., output images smaller than dstSize 
 *            are not allowed. 
 *   rcpRatioy - y direction scaling control parameter, specified in terms of 
 *            a reciprocal resize ratio using a Q16 representation. Valid in 
 *            the range [1, yrr_max], where yrr_max = (OMX_INT) ((( (OMX_F32) 
 *            (srcSize.height&~1)-1) / ((dstSize.height&~1)-1)) * (1<< 16) 
 *            +0.5). Setting rcpRatioy = yrr_max guarantees that the output 
 *            image size will be exactly dstSize; otherwise for values less 
 *            than yrr_max the bottom of the output image will be clipped 
 *            since the output image will be larger than dstSize.  Expansion 
 *            in the y direction occurs for values of rcpRatioy < 65536; 
 *            contraction in the y direction occurs for values > 65536.  To 
 *            avoid clipping, use the value yrr_max, which will ensure that 
 *            the output image height == dstSize.height.  Values larger than 
 *            yrr_max are invalid, i.e., output images smaller than dstSize 
 *            are not allowed. 
 *
 *            Size of output image, dstSize is the size of destination image 
 *            before rotation, i.e., dstSize is the size of the image after 
 *            resizing. 
 *
 * Output Arguments:
 *   
 *   pDst - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr420 output planes for <omxIPCS_YCbCr420RszRot_U8_P3R> 
 *            or YCbCr422 output planes for <omxIPCS_YCbCr422RszRot_U8_P3R>. 
 *            The pointer pDst[0] must be aligned on a 4-byte boundary. 
 *
 * Return Value:
 *    If the function runs without error, it returns OMX_Sts_NoErr. 
 *    OMX_Sts_BadArgErr, if any of the following cases occurs: 
 *    -   Any pointer is NULL 
 *    -   srcSize.width, srcSize.height <= 0
 *    -   dstSize.width, dstSize.height <= 0
 *    -   rcpRatioy is outside of the range [1, yrr_max], where yrr_max = 
 *              (OMX_INT) ((( (OMX_F32) (srcSize.height&~1)-1) / 
 *              ((dstSize.height&~1)-1)) * (1<< 16) + 0.5). 
 *    -   rcpRatiox is outside of the range [1, xrr_max], where xrr_max = 
 *              (OMX_INT) ((( (OMX_F32) ((srcSize.width&~1)-1) / 
 *              ((dstSize.width&~1)-1)) * (1<<16) + 0.5). 
 *    -   pDst[0] is not aligned on a 4-byte boundary 
 *    -   pSrc[0] is not aligned on a 4-byte boundary 
 *    -   dstSize.width or dstSize.height is not even 
 *    -   srcStep[0], srcStep[1], srcStep[2]or dstStep[0], dstStep[1], 
 *              dstStep[2]is less than 1 
 *    -   srcStep[0] is not multiple of 4 
 *    -   dstStep[0] is not multiple of 4 
 *    -   srcSize.width is larger than srcStep[0]; srcSize.width>>1 is larger 
 *              than srcStep[1] or srcStep[2]. 
 *    -   interpolation contains an invalid value
 *    -   rotation contains an invalid value
 *    -   dstSize.height of output image is larger than dstStep[0], 
 *    -   dstSize.height>>1 of output image is larger than dstStep[1] or 
 *              dstStep[2] when rotation is OMX_IP_ROTATE90L or 
 *              OMX_IP_ROTATE90R; 
 *    -   dstSize.width of output image is larger than dstStep[0], 
 *    -   dstSize.width>>1 of output image is larger than dstStep[1] or dstStep[2]
 *        when other valid rotation options. 
 *
 */
OMXResult omxIPCS_YCbCr420RszRot_U8_P3R (
    const OMX_U8 *pSrc[3],
    OMX_INT srcStep[3],
    OMXSize srcSize,
    OMX_U8 *pDst[3],
    OMX_INT dstStep[3],
    OMXSize dstSize,
    OMXIPInterpolation interpolation,
    OMXIPRotation rotation,
    OMX_INT rcpRatiox,
    OMX_INT rcpRatioy
);



/**
 * Function:  omxIPCS_YCbCr422RszRot_U8_P3R   (4.4.3.4.2)
 *
 * Description:
 * This function combines two atomic image processing kernels into a single 
 * function. The following sequence of operations is applied: 
 *
 *   1. Resize. The input image of dimension srcSize is rescaled according 
 *   to the Q16 reciprocal ratio scaling control parameters rcpRatiox and 
 *   rcpRatioy using the interpolation/decimation methodology specified by 
 *   the control parameter interpolation. Nearest neighbor and bilinear 
 *   interpolation schemes are supported.  The rescaled image is clipped to 
 *   dstSize using a clipping rectangle, the origin of which coincides with 
 *   the top, left corner of the input image, i.e., pixels are discarded 
 *   along the right and bottom edges. 
 *
 *   2. Rotation. The output image is rotated with respect to the input image
 *   according to the control parameter rotation. 
 *
 * The input data should be YCbCr420 planar format for <omxIPCS_YCbCr420RszRot_U8_P3R> 
 * and YCbCr422 planar format for <omxIPCS_YCbCr422RszRot_U8_P3R>. 
 *
 * Input Arguments:
 *   
 *   pSrc - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr420 input planes for <omxIPCS_YCbCr420RszRot_U8_P3R> 
 *            and YCbCr422 input planes for <omxIPCS_YCbCr422RszRot_U8_P3R>. 
 *            The pointer pSrc[0] must be aligned on a 4-byte boundary. 
 *   srcStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the input image planes; the 
 *            parameter srcStep[0] must be a multiple of 4. 
 *   dstStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the output image planes; the 
 *            parameter dstStep[0] must be a multiple of 4. 
 *   srcSize - dimensions, in pixels, of the source image 
 *   dstSize - dimensions, in pixels, of the destination image (before 
 *            applying rotation to the resized image).  The parameters 
 *            dstSize.width and dstSize.height must be even. 
 *   interpolation - interpolation methodology control parameter; must take 
 *            one of the following values: OMX_IP_NEAREST, or OMX_IP_BILINEAR 
 *            for nearest neighbor or bilinear interpolation, respectively 
 *   rotation - rotation control parameter; must be set to one of the 
 *            following pre-defined values: . OMX_IP_DISABLE, 
 *            OMX_IP_ROTATE90L, OMX_IP_ROTATE90R, OMX_IP_ROTATE180, 
 *            OMX_IP_FLIP_HORIZONTAL, or OMX_IP_FLIP_VERTICAL 
 *   rcpRatiox - x direction scaling control parameter, specified in terms of 
 *            a reciprocal resize ratio using a Q16 representation. Valid in 
 *            the range [1, xrr_max], where xrr_max = (OMX_INT) ((( (OMX_F32) 
 *            ((srcSize.width&~1)-1) / ((dstSize.width&~1)-1)) * (1<<16) 
 *            +0.5). Setting rcpRatiox = xrr_max guarantees that the output 
 *            image size will be exactly dstSize; otherwise for values less 
 *            than xrr_max the right hand side of the image will be clipped 
 *            since the output image will extend beyond dstSize.  Expansion in 
 *            the x direction occurs for values of rcpRatiox < 65536; 
 *            contraction in the x direction occurs for values > 65536.  To 
 *            avoid clipping, use the value xrr_max, which will ensure that 
 *            the output image width == dstSize.width.  Values larger than 
 *            xrr_max are invalid, i.e., output images smaller than dstSize 
 *            are not allowed. 
 *   rcpRatioy - y direction scaling control parameter, specified in terms of 
 *            a reciprocal resize ratio using a Q16 representation. Valid in 
 *            the range [1, yrr_max], where yrr_max = (OMX_INT) ((( (OMX_F32) 
 *            (srcSize.height&~1)-1) / ((dstSize.height&~1)-1)) * (1<< 16) 
 *            +0.5). Setting rcpRatioy = yrr_max guarantees that the output 
 *            image size will be exactly dstSize; otherwise for values less 
 *            than yrr_max the bottom of the output image will be clipped 
 *            since the output image will be larger than dstSize.  Expansion 
 *            in the y direction occurs for values of rcpRatioy < 65536; 
 *            contraction in the y direction occurs for values > 65536.  To 
 *            avoid clipping, use the value yrr_max, which will ensure that 
 *            the output image height == dstSize.height.  Values larger than 
 *            yrr_max are invalid, i.e., output images smaller than dstSize 
 *            are not allowed. 
 *
 * Output Arguments:
 *   
 *   pDst - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr420 output planes for <omxIPCS_YCbCr420RszRot_U8_P3R> 
 *            or YCbCr422 output planes for <omxIPCS_YCbCr422RszRot_U8_P3R>. 
 *            The pointer pDst[0] must be aligned on a 4-byte boundary. 
 *
 * Return Value:
 *    If the function runs without error, it returns OMX_Sts_NoErr. 
 *    OMX_Sts_BadArgErr, if any of the following cases occurs: 
 *    -   Any pointer is NULL 
 *    -   srcSize.width, srcSize.height <= 0
 *    -   dstSize.width, dstSize.height <= 0
 *    -   rcpRatioy is outside of the range [1, yrr_max], where yrr_max = 
 *              (OMX_INT) ((( (OMX_F32) (srcSize.height&~1)-1) / 
 *              ((dstSize.height&~1)-1)) * (1<< 16) + 0.5). 
 *    -   rcpRatiox is outside of the range [1, xrr_max], where xrr_max = 
 *              (OMX_INT) ((( (OMX_F32) ((srcSize.width&~1)-1) / 
 *              ((dstSize.width&~1)-1)) * (1<<16) + 0.5). 
 *    -   pDst[0] is not aligned on a 4-byte boundary 
 *    -   pSrc[0] is not aligned on a 4-byte boundary 
 *    -   dstSize.width or dstSize.height is not even 
 *    -   srcStep[0], srcStep[1], srcStep[2]or dstStep[0], dstStep[1], 
 *              dstStep[2]is less than 1 
 *    -   srcStep[0] is not multiple of 4 
 *    -   dstStep[0] is not multiple of 4 
 *    -   srcSize.width is larger than srcStep[0]; srcSize.width>>1 is larger 
 *              than srcStep[1] or srcStep[2]. 
 *    -   interpolation contains an invalid value
 *    -   rotation contains an invalid value
 *    -   dstSize.height of output image is larger than dstStep[0], 
 *    -   dstSize.height>>1 of output image is larger than dstStep[1] or 
 *              dstStep[2] when rotation is OMX_IP_ROTATE90L or 
 *              OMX_IP_ROTATE90R; 
 *    -   dstSize.width of output image is larger than dstStep[0], 
 *    -   dstSize.width>>1 of output image is larger than dstStep[1] or dstStep[2]
 *        when other valid rotation options. 
 *
 */
OMXResult omxIPCS_YCbCr422RszRot_U8_P3R (
    const OMX_U8 *pSrc[3],
    OMX_INT srcStep[3],
    OMXSize srcSize,
    OMX_U8 *pDst[3],
    OMX_INT dstStep[3],
    OMXSize dstSize,
    OMXIPInterpolation interpolation,
    OMXIPRotation rotation,
    OMX_INT rcpRatiox,
    OMX_INT rcpRatioy
);



/**
 * Function:  omxIPCS_YCbCr420RszCscRotBGR_U8_P3C3R   (4.4.3.5.2)
 *
 * Description:
 * This function combines several atomic image processing kernels into a 
 * single function. In particular, the following sequence of operations is 
 * applied to the input YCbCr image: 
 *
 *   1. Resize. The input image of dimension srcSize is rescaled according 
 *   to the Q16 reciprocal ratio scaling control parameters rcpRatiox and 
 *   rcpRatioy using the interpolation/decimation methodology specified by 
 *   the control parameter interpolation. Nearest neighbor and bilinear 
 *   interpolation schemes are supported.  The rescaled image is clipped to 
 *   dstSize using a clipping rectangle, the origin of which coincides with 
 *   the top, left corner of the input image, i.e., pixels are discarded 
 *   along the right and bottom edges. 
 *
 *   2. Color space conversion.  Following scaling, color space conversion 
 *   from either YCbCr420 or YCbCr422 to BGR is applied according to the 
 *   control parameter colorConversion. The following target BGR color 
 *   spaces are supported:  BGR565, BGR888, BGR555, or BGR444. 
 *
 *   3. Rotation. After color space conversion, the output image is rotated 
 *   with respect to the input image according to the control parameter 
 *   rotation. 
 *
 * The input data should be in YCbCr420 planar format for the 
 * function <omxIPCS_YCbCr420RszCscRotBGR_U8_P3CR> or YCbCr422 planar format 
 * for the function <omxIPCS_YCbCr422RszCscRotBGR_U8_P3CR>.
 * The starting address of pDst must be aligned at 8-byte boundary; and 
 * dstStep must be multiple of 8 when output format is BGR444/555/565, 
 * and dstStep must be multiple of 2 when output format is BGR888. 
 * The starting address of pSrc[0] must be aligned at a 4-byte boundary, 
 * pSrc[1] must be aligned at a 2-byte boundary and pSrc[2] must be aligned 
 * at a 2-byte boundary; and srcStep[0] must be multiple of 4, srcStep[1] 
 * must be multiple of 2 and srcStep[2] must be multiple of 2. 
 * 
 * The parameter dstSize specifies the size of the image after the 
 * resizing operation, but prior to the rotation operation. 
 *
 * Input Arguments:
 *   
 *   pSrc - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr420 or YCbCr422 input planes 
 *   srcStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the input image planes 
 *   dstStep - distance, in bytes, between the start of lines in the 
 *            destination image 
 *   srcSize - dimensions, in pixels, of the source image 
 *   dstSize - dimensions, in pixels, of the destination image (before 
 *            applying rotation to the resizing image) 
 *   interpolation - interpolation methodology control parameter; must take 
 *            one of the following values: OMX_IP_NEAREST, or OMX_IP_BILINEAR 
 *            for nearest neighbor or bilinear interpolation, respectively. 
 *   colorConversion - color conversion control parameter; must be set to one 
 *            of the following pre defined values: OMX_IP_BGR565, 
 *            OMX_IP_BGR555, OMX_IP_BGR444, or OMX_IP_BGR888. 
 *   rotation - rotation control parameter; must be set to one of the 
 *            following pre-defined values: OMX_IP_DISABLE OMX_IP_ROTATE90L 
 *            OMX_IP_ROTATE90R OMX_IP_ROTATE180 OMX_IP_FLIP_HORIZONTAL 
 *            OMX_IP_FLIP_VERTICAL Counter-clockwise rotation is denoted by 
 *            the  L  postfix, and clockwise rotation is denoted by the  R  
 *            postfix. A horizontal flip creates a mirror image with respect 
 *            to the vertical image axis, i.e.,  
 *                     "bow" -> horizontal flip -> "wod" 
 *            and a vertical flip creates a mirror image with respect to the 
 *            horizontal image axis, i.e.,
 *                     "bow" -> vertical flip -> "pom"
 *   rcpRatiox - x direction scaling control parameter, specified in terms of 
 *            a reciprocal resize ratio using a Q16 representation. Valid in 
 *            the range [1, xrr_max], where xrr_max = (OMX_INT) ((( (OMX_F32) 
 *            ((srcSize.width&~1)-1) / ((dstSize.width&~1)-1)) * (1<<16) 
 *            +0.5). Setting rcpRatiox = xrr_max guarantees that the output 
 *            image size will be exactly dstSize; otherwise for values less 
 *            than xrr_max the right hand side of the image will be clipped 
 *            since the output image will extend beyond dstSize.  Expansion in 
 *            the x direction occurs for values of rcpRatiox < 65536; 
 *            contraction in the x direction occurs for values > 65536.  To 
 *            avoid clipping, use the value xrr_max, which will ensure that 
 *            the output image width == dstSize.width.  Values larger than 
 *            xrr_max are invalid, i.e., output images smaller than dstSize 
 *            are not allowed. 
 *   rcpRatioy - y direction scaling control parameter, specified in terms of 
 *            a reciprocal resize ratio using a Q16 representation. Valid in 
 *            the range [1, yrr_max], where yrr_max = (OMX_INT) ((( (OMX_F32) 
 *            (srcSize.height&~1)-1) / ((dstSize.height&~1)-1)) * (1<< 16) 
 *            +0.5). Setting rcpRatioy = yrr_max guarantees that the output 
 *            image size will be exactly dstSize; otherwise for values less 
 *            than yrr_max the bottom of the output image will be clipped 
 *            since the output image will be larger than dstSize.  Expansion 
 *            in the y direction occurs for values of rcpRatioy < 65536; 
 *            contraction in the y direction occurs for values > 65536.  To 
 *            avoid clipping, use the value yrr_max, which will ensure that 
 *            the output image height == dstSize.height.  Values larger than 
 *            yrr_max are invalid, i.e., output images smaller than dstSize 
 *            are not allowed. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the start of the buffer containing the resized, 
 *            color-converted, and rotated output image. 
 *
 * Return Value:
 *    If the function runs without error, it returns OMX_Sts_NoErr. 
 *    If one of the following cases occurs, the function returns 
 *              OMX_Sts_BadArgErr: 
 *    -   any pointer is NULL 
 *    -   rcpRatioy is outside of the range [1, yrr_max], where yrr_max = 
 *              (OMX_INT) ((( (OMX_F32) (srcSize.height&~1)-1) / 
 *              ((dstSize.height&~1)-1)) * (1<< 16) + 0.5). 
 *    -   rcpRatiox is outside of the range [1, xrr_max], where xrr_max = 
 *              (OMX_INT) ((( (OMX_F32) ((srcSize.width&~1)-1) / 
 *              ((dstSize.width&~1)-1)) * (1<<16) + 0.5). 
 *    -   srcSize.width or srcSize.height <=0
 *    -   dstSize.width or dstSize.height <=0
 *    -   pDst is not aligned at 8 bytes boundary, 
 *    -   pSrc[0] is not aligned at 4 bytes boundary, 
 *    -   pSrc[1] is not aligned at 2 bytes boundary, or 
 *    -   pSrc[2] is not aligned at 2 bytes boundary. 
 *    -   srcStep[0], srcStep[1], srcStep[2] or dstStep is less than 1. 
 *    -   srcStep[0] is not a multiple of 4, 
 *    -   srcStep[1] is not multiple of 2, or 
 *    -   srcStep[2] is not multiple of 2. 
 *    -   dstStep is not a multiple of 8 when colorConversion is OMX_IP_BGR565, 
 *              OMX_IP_BGR555, or OMX_IP_BGR444; 
 *    -   dstStep is not multiple of 2 when colorConversion is OMX_IP_BGR888. 
 *    -   srcSize.width is larger than srcStep[0]; 
 *    -   srcSize.width>>1 is larger than half of srcStep[1] or srcStep[2]. 
 *    -   interpolation contains an invalid value
 *    -   colorConversion contains an invalid value
 *    -   rotation contains an invalid value
 *    -   dstSize.height * bytes/pixel of output image is larger than dstStep 
 *              when rotation is OMX_IP_ROTATE90L or OMX_IP_ROTATE90R; 
 *    -   dstSize.width * bytes/pixel of output image is larger than 
 *              dstStep when other valid rotation options. 
 *
 */
OMXResult omxIPCS_YCbCr420RszCscRotBGR_U8_P3C3R (
    const OMX_U8 *pSrc[3],
    OMX_INT srcStep[3],
    OMXSize srcSize,
    void *pDst,
    OMX_INT dstStep,
    OMXSize dstSize,
    OMXIPColorSpace colorConversion,
    OMXIPInterpolation interpolation,
    OMXIPRotation rotation,
    OMX_INT rcpRatiox,
    OMX_INT rcpRatioy
);



/**
 * Function:  omxIPCS_YCbCr422RszCscRotBGR_U8_P3C3R   (4.4.3.5.2)
 *
 * Description:
 * This function combines several atomic image processing kernels into a 
 * single function. In particular, the following sequence of operations is 
 * applied to the input YCbCr image: 
 *
 *   1. Resize. The input image of dimension srcSize is rescaled according 
 *   to the Q16 reciprocal ratio scaling control parameters rcpRatiox and 
 *   rcpRatioy using the interpolation/decimation methodology specified by 
 *   the control parameter interpolation. Nearest neighbor and bilinear 
 *   interpolation schemes are supported.  The rescaled image is clipped to 
 *   dstSize using a clipping rectangle, the origin of which coincides with 
 *   the top, left corner of the input image, i.e., pixels are discarded 
 *   along the right and bottom edges. 
 *
 *   2. Color space conversion.  Following scaling, color space conversion 
 *   from either YCbCr420 or YCbCr422 to BGR is applied according to the 
 *   control parameter colorConversion. The following target BGR color 
 *   spaces are supported:  BGR565, BGR888, BGR555, or BGR444. 
 *
 *   3. Rotation. After color space conversion, the output image is rotated 
 *   with respect to the input image according to the control parameter 
 *   rotation. 
 *
 * The input data should be in YCbCr420 planar format for the 
 * function <omxIPCS_YCbCr420RszCscRotBGR_U8_P3CR> or YCbCr422 planar format 
 * for the function <omxIPCS_YCbCr422RszCscRotBGR_U8_P3CR>.
 * The starting address of pDst must be aligned at 8-byte boundary; and 
 * dstStep must be multiple of 8 when output format is BGR444/555/565, 
 * and dstStep must be multiple of 2 when output format is BGR888. 
 * The starting address of pSrc[0] must be aligned at a 4-byte boundary, 
 * pSrc[1] must be aligned at a 2-byte boundary and pSrc[2] must be aligned 
 * at a 2-byte boundary; and srcStep[0] must be multiple of 4, srcStep[1] 
 * must be multiple of 2 and srcStep[2] must be multiple of 2. 
 * 
 * The parameter dstSize specifies the size of the image after the 
 * resizing operation, but prior to the rotation operation. 
 *
 * Input Arguments:
 *   
 *   pSrc - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr420 or YCbCr422 input planes 
 *   srcStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the input image planes 
 *   dstStep - distance, in bytes, between the start of lines in the 
 *            destination image 
 *   srcSize - dimensions, in pixels, of the source image 
 *   dstSize - dimensions, in pixels, of the destination image (before 
 *            applying rotation to the resizing image) 
 *   interpolation - interpolation methodology control parameter; must take 
 *            one of the following values: OMX_IP_NEAREST, or OMX_IP_BILINEAR 
 *            for nearest neighbor or bilinear interpolation, respectively. 
 *   colorConversion - color conversion control parameter; must be set to one 
 *            of the following pre defined values: OMX_IP_BGR565, 
 *            OMX_IP_BGR555, OMX_IP_BGR444, or OMX_IP_BGR888. 
 *   rotation - rotation control parameter; must be set to one of the 
 *            following pre-defined values: OMX_IP_DISABLE OMX_IP_ROTATE90L 
 *            OMX_IP_ROTATE90R OMX_IP_ROTATE180 OMX_IP_FLIP_HORIZONTAL 
 *            OMX_IP_FLIP_VERTICAL Counter-clockwise rotation is denoted by 
 *            the "L" postfix, and clockwise rotation is denoted by the "R"
 *            postfix. A horizontal flip creates a  mirror  image with respect 
 *            to the vertical image axis, i.e.,  
 *                     "bow" -> horizontal flip -> "wod" 
 *            and a vertical flip creates a mirror image with respect to the 
 *            horizontal image axis, i.e.,
 *                     "bow" -> vertical flip -> "pom"
 *   rcpRatiox - x direction scaling control parameter, specified in terms of 
 *            a reciprocal resize ratio using a Q16 representation. Valid in 
 *            the range [1, xrr_max], where xrr_max = (OMX_INT) ((( (OMX_F32) 
 *            ((srcSize.width&~1)-1) / ((dstSize.width&~1)-1)) * (1<<16) 
 *            +0.5). Setting rcpRatiox = xrr_max guarantees that the output 
 *            image size will be exactly dstSize; otherwise for values less 
 *            than xrr_max the right hand side of the image will be clipped 
 *            since the output image will extend beyond dstSize.  Expansion in 
 *            the x direction occurs for values of rcpRatiox < 65536; 
 *            contraction in the x direction occurs for values > 65536.  To 
 *            avoid clipping, use the value xrr_max, which will ensure that 
 *            the output image width == dstSize.width.  Values larger than 
 *            xrr_max are invalid, i.e., output images smaller than dstSize 
 *            are not allowed. 
 *   rcpRatioy - y direction scaling control parameter, specified in terms of 
 *            a reciprocal resize ratio using a Q16 representation. Valid in 
 *            the range [1, yrr_max], where yrr_max = (OMX_INT) ((( (OMX_F32) 
 *            (srcSize.height&~1)-1) / ((dstSize.height&~1)-1)) * (1<< 16) 
 *            +0.5). Setting rcpRatioy = yrr_max guarantees that the output 
 *            image size will be exactly dstSize; otherwise for values less 
 *            than yrr_max the bottom of the output image will be clipped 
 *            since the output image will be larger than dstSize.  Expansion 
 *            in the y direction occurs for values of rcpRatioy < 65536; 
 *            contraction in the y direction occurs for values > 65536.  To 
 *            avoid clipping, use the value yrr_max, which will ensure that 
 *            the output image height == dstSize.height.  Values larger than 
 *            yrr_max are invalid, i.e., output images smaller than dstSize 
 *            are not allowed. 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the start of the buffer containing the resized, 
 *            color-converted, and rotated output image. 
 *
 * Return Value:
 *    If the function runs without error, it returns OMX_Sts_NoErr. 
 *    If one of the following cases occurs, the function returns 
 *              OMX_Sts_BadArgErr: 
 *    -   any pointer is NULL 
 *    -   rcpRatioy is outside of the range [1, yrr_max], where yrr_max = 
 *              (OMX_INT) ((( (OMX_F32) (srcSize.height&~1)-1) / 
 *              ((dstSize.height&~1)-1)) * (1<< 16) + 0.5). 
 *    -   rcpRatiox is outside of the range [1, xrr_max], where xrr_max = 
 *              (OMX_INT) ((( (OMX_F32) ((srcSize.width&~1)-1) / 
 *              ((dstSize.width&~1)-1)) * (1<<16) + 0.5). 
 *    -   srcSize.width or srcSize.height <=0
 *    -   dstSize.width or dstSize.height <=0
 *    -   pDst is not aligned at 8 bytes boundary, 
 *    -   pSrc[0] is not aligned at 4 bytes boundary, 
 *    -   pSrc[1] is not aligned at 2 bytes boundary, or 
 *    -   pSrc[2] is not aligned at 2 bytes boundary. 
 *    -   srcStep[0], srcStep[1], srcStep[2] or dstStep is less than 1. 
 *    -   srcStep[0] is not a multiple of 4, 
 *    -   srcStep[1] is not multiple of 2, or 
 *    -   srcStep[2] is not multiple of 2. 
 *    -   dstStep is not a multiple of 8 when colorConversion is OMX_IP_BGR565, 
 *              OMX_IP_BGR555, or OMX_IP_BGR444; 
 *    -   dstStep is not multiple of 2 when colorConversion is OMX_IP_BGR888. 
 *    -   srcSize.width is larger than srcStep[0]; 
 *    -   srcSize.width>>1 is larger than half of srcStep[1] or srcStep[2]. 
 *    -   interpolation contains an invalid value
 *    -   colorConversion contains an invalid value
 *    -   rotation contains an invalid value
 *    -   dstSize.height * bytes/pixel of output image is larger than dstStep 
 *              when rotation is OMX_IP_ROTATE90L or OMX_IP_ROTATE90R; 
 *    -   dstSize.width * bytes/pixel of output image is larger than 
 *              dstStep when other valid rotation options. 
 *
 */
OMXResult omxIPCS_YCbCr422RszCscRotBGR_U8_P3C3R (
    const OMX_U8 *pSrc[3],
    OMX_INT srcStep[3],
    OMXSize srcSize,
    void *pDst,
    OMX_INT dstStep,
    OMXSize dstSize,
    OMXIPColorSpace colorConversion,
    OMXIPInterpolation interpolation,
    OMXIPRotation rotation,
    OMX_INT rcpRatiox,
    OMX_INT rcpRatioy
);



/**
 * Function:  omxIPCS_CbYCrY422ToYCbCr420Rotate_U8_C2P3R   (4.4.3.6.1)
 *
 * Description:
 * CbYCrY422 to YCbCr420 planar format conversion with rotation function.  
 * This function decimates the color space of the input image from CbYCrY 422 
 * to YCbCr 420, applies an optional rotation of -90, +90, or 180 degrees, and 
 * then rearranges the data from the pixel-oriented input format to a planar 
 * output format. 
 * The size of output image: if roiSize.width or roiSize.height cannot be 
 * divided by 8 exactly, it will be cut to be a multiple of 8. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the start of the buffer containing the pixel-oriented 
 *            CbYCrY422 input; must be aligned on an 8-byte boundary. 
 *   srcStep - distance, in bytes, between the start of lines in the source 
 *            image; must be a multiple of 8. 
 *   dstStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the output image planes.  The 
 *            parameter dstStep[0] must be a multiple of 8; the parameters 
 *            dstStep[1] and dstStep[2] must be multiples of 4. 
 *   roiSize - dimensions, in pixels, of the source and destination regions 
 *            of interest 
 *   rotation - rotation control parameter; must be set to one of the 
 *            following pre-defined values: OMX_IP_DISABLE, OMX_IP_ROTATE90L, 
 *            OMX_IP_ROTATE90R, or OMX_IP_ROTATE180. OutputArguments 
 *
 * Output Arguments:
 *   pDst - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr420 output planes. The pointer pDst[0] must be aligned 
 *            on an 8-byte boundary.  The pointers pDst[1] and pDst[2]must be 
 *            aligned on 4-byte boundaries. 
 *
 * Return Value:
 *    If the function runs without error, it returns OMX_Sts_NoErr 
 *    If any of the following cases occurs, the function returns 
 *    OMX_Sts_BadArgErr: 
 *    -   pSrc, pDst[0], pDst[1], or pDst[2] is NULL 
 *    -   pSrc or pDst[0] is not aligned at 8 bytes boundary 
 *    -   pDst[1] or pDst[2] is not aligned at 4-byte boundary 
 *    -   srcStep, dstStep[1], dstStep[2], or dstStep[3] is less than 1 
 *    -   srcStep or dstStep[0] is not multiple of 8 
 *    -   dstStep[1] or dstStep[2] is not multiple of 4 
 *    -   roiSize.width is larger than half of srcStep 
 *    -   rotation contains an invalid control parameter
 *    -   dstStep[0] is less than roiSize.width 
 *    -   dstStep[1] or dstStep[2] is less than half roiSize.width 
 *    -   roiSize.width or roiSize.height is less than 8 
 *
 */
OMXResult omxIPCS_CbYCrY422ToYCbCr420Rotate_U8_C2P3R (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_U8 *pDst[3],
    OMX_INT dstStep[3],
    OMXSize roiSize,
    OMXIPRotation rotation
);



/**
 * Function:  omxIPCS_YCbCr422ToYCbCr420Rotate_U8_P3R   (4.4.3.6.2)
 *
 * Description:
 * This function decimates the color space of the input image from YCbCr 422 
 * planar data to YCbCr 420 planar data, and then applies an optional rotation 
 * of -90, +90, or 180 degrees. The difference between this function and 
 * omxIPCS_CbYCrY422ToYCbCr420Rotate_U8_C2P3R is that this function supports 
 * the input YCbCr422 format in planar order. 
 *
 * 
 * If the size of the output image, roiSize.width or roiSize.height, cannot 
 * be divided by 8 exactly, it will be cut to be multiple of 8. 
 *
 * Input Arguments:
 *   
 *   pSrc - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr422 input planes. The pointer pSrc[0] must be aligned 
 *            on an 8-byte boundary; the pointers pSrc[1] and pSrc[2] must be 
 *            aligned on a 4-byte boundaries. 
 *   srcStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the input image planes.  The 
 *            parameter srcStep[0] must be a multiple of 8; the parameters 
 *            srcStep[1] and srcStep[2] must multiples of 4. 
 *   dstStep - a 3-element vector containing the distance, in bytes, between 
 *            the start of lines in each of the output image planes.  The 
 *            parameter dstStep[0] must be a multiple of 8; the parameters 
 *            dstStep[1] and dstStep[2] must multiples of 4. 
 *   roiSize - dimensions, in pixels, of the source and destination regions 
 *            of interest 
 *   rotation - rotation control parameter; must be set to one of the 
 *            following pre-defined values: OMX_IP_DISABLE, OMX_IP_ROTATE90L, 
 *            OMX_IP_ROTATE90R, or OMX_IP_ROTATE180. 
 *
 * Output Arguments:
 *   
 *   pDst - a 3-element vector containing pointers to the start of each of 
 *            the YCbCr420 output planes. The pointer pDst[0] must be aligned 
 *            on an 8-byte boundary; the pointers pDst[1] and pDst[2]must be 
 *            aligned on a 4-byte boundaries. 
 *
 * Return Value:
 *    If the function runs without error, it returns OMX_Sts_NoErr 
 *    The function returns OMX_Sts_BadArgErr if one or more of the 
 *              following occurs: 
 *    -   any pointer is NULL 
 *    -   pSrc[0] or pDst[0] is not aligned on an 8-byte boundary 
 *    -   pSrc[1], pSrc[2], pDst[1] or pDst[2] is not aligned on 
 *        a 4-byte boundary 
 *    -   any of the steps is less than 1 
 *    -   srcStep[0] or dstStep[0] is not multiple of 8 
 *    -   srcStep[1], srcStep[2], dstStep[1] or dstStep[2] is not multiple of 4 
 *    -   roiSize.width is larger than srcStep[0] 
 *    -   roiSize.width is larger than twice srcStep[1] or twice srcStep[2] 
 *    -   rotation contains an invalid value
 *    -   dstStep[0] is less than roiSize.width of downscaled, color-converted 
 *              and rotated image 
 *    -   dstStep[1] or dstStep[2] is less than half roiSize.width 
 *    -   roiSize.width or roiSize.height is less than 8. 
 *
 */
OMXResult omxIPCS_YCbCr422ToYCbCr420Rotate_U8_P3R (
    const OMX_U8 *pSrc[3],
    OMX_INT srcStep[3],
    OMX_U8 *pDst[3],
    OMX_INT dstStep[3],
    OMXSize roiSize,
    OMXIPRotation rotation
);



/**
 * Function:  omxIPCS_BGR888ToYCbCr444LS_MCU_U8_S16_C3P3   (4.4.3.7.3)
 *
 * Description:
 * These functions convert an input BGR888 MCU to one of the following 
 * sub-sampled color spaces with level-shift:  YCbCr4:4:4, YCbCr4:2:2, and 
 * YCbCr4:2:0.  The parameter pDstMCU[0] must point to the start of a buffer 
 * with sufficient free space to contain the particular number of contiguously 
 * stored 8x8 Y blocks that constitute a complete MCU given the output chroma 
 * sub-sampling scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, 
 * or 4:2:0 output, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source image data buffer.  The source image data 
 *            are stored in interleaved order as BGRBGRBGR  The image data 
 *            buffer pSrc can support bottom-up storage formats.  For 
 *            bottom-up images, srcStep can be less than 0. 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image;  can be less than 0 to support bottom-up 
 *            storage format. 
 *
 * Output Arguments:
 *   
 *   pDstMCU[3] - output MCU pointers.  The parameter pDstMCU[0] must point 
 *            to the start of a buffer with sufficient free space to contain 
 *            the particular number of contiguously stored 8x8 Y blocks that 
 *            constitute a complete MCU given the output chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, or 
 *            4:2:0 output, respectively. The parameter pDstMCU[1] points to 
 *            the Cb block, and the parameter pDstMCU[2] points to the Cr 
 *            block.  Output components are expressed using a Q8 
 *            representation and are bounded on the interval [-128, 127].  The 
 *            buffers referenced by pDstMCU[] support top-down storage format 
 *            only, and all pointers pDstMCU[0..2] must be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - one or more of the following bad argument 
 *              conditions was detected: 
 *    -   a pointer was NULL 
 *    -   the absolute value of srcStep was smaller than 24 for YCbCr 444,
 *        or 48 for YCbCr422/ YCbCr420 
 *    -   the start address of each pointer in pDstMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_BGR888ToYCbCr444LS_MCU_U8_S16_C3P3 (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_S16 *pDstMCU[3]
);



/**
 * Function:  omxIPCS_BGR888ToYCbCr422LS_MCU_U8_S16_C3P3   (4.4.3.7.3)
*
 * Description:
 * These functions convert an input BGR888 MCU to one of the following 
 * sub-sampled color spaces with level-shift:  YCbCr4:4:4, YCbCr4:2:2, and 
 * YCbCr4:2:0.  The parameter pDstMCU[0] must point to the start of a buffer 
 * with sufficient free space to contain the particular number of contiguously 
 * stored 8x8 Y blocks that constitute a complete MCU given the output chroma 
 * sub-sampling scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, 
 * or 4:2:0 output, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source image data buffer.  The source image data 
 *            are stored in interleaved order as BGRBGRBGR  The image data 
 *            buffer pSrc can support bottom-up storage formats.  For 
 *            bottom-up images, srcStep can be less than 0. 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image;  can be less than 0 to support bottom-up 
 *            storage format. 
 *
 * Output Arguments:
 *   
 *   pDstMCU[3] - output MCU pointers.  The parameter pDstMCU[0] must point 
 *            to the start of a buffer with sufficient free space to contain 
 *            the particular number of contiguously stored 8x8 Y blocks that 
 *            constitute a complete MCU given the output chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, or 
 *            4:2:0 output, respectively. The parameter pDstMCU[1] points to 
 *            the Cb block, and the parameter pDstMCU[2] points to the Cr 
 *            block.  Output components are expressed using a Q8 
 *            representation and are bounded on the interval [-128, 127].  The 
 *            buffers referenced by pDstMCU[] support top-down storage format 
 *            only, and all pointers pDstMCU[0..2] must be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - one or more of the following bad argument 
 *              conditions was detected: 
 *    -   a pointer was NULL 
 *    -   the absolute value of srcStep was smaller than 24 for YCbCr 444,
 *        or 48 for YCbCr422/ YCbCr420 
 *    -   the start address of each pointer in pDstMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_BGR888ToYCbCr422LS_MCU_U8_S16_C3P3 (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_S16 *pDstMCU[3]
);



/**
 * Function:  omxIPCS_BGR888ToYCbCr420LS_MCU_U8_S16_C3P3   (4.4.3.7.3)
 *
 * Description:
 * These functions convert an input BGR888 MCU to one of the following 
 * sub-sampled color spaces with level-shift:  YCbCr4:4:4, YCbCr4:2:2, and 
 * YCbCr4:2:0.  The parameter pDstMCU[0] must point to the start of a buffer 
 * with sufficient free space to contain the particular number of contiguously 
 * stored 8x8 Y blocks that constitute a complete MCU given the output chroma 
 * sub-sampling scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, 
 * or 4:2:0 output, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the source image data buffer.  The source image data 
 *            are stored in interleaved order as BGRBGRBGR  The image data 
 *            buffer pSrc can support bottom-up storage formats.  For 
 *            bottom-up images, srcStep can be less than 0. 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image;  can be less than 0 to support bottom-up 
 *            storage format. 
 *
 * Output Arguments:
 *   
 *   pDstMCU[3] - output MCU pointers.  The parameter pDstMCU[0] must point 
 *            to the start of a buffer with sufficient free space to contain 
 *            the particular number of contiguously stored 8x8 Y blocks that 
 *            constitute a complete MCU given the output chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, or 
 *            4:2:0 output, respectively. The parameter pDstMCU[1] points to 
 *            the Cb block, and the parameter pDstMCU[2] points to the Cr 
 *            block.  Output components are expressed using a Q8 
 *            representation and are bounded on the interval [-128, 127].  The 
 *            buffers referenced by pDstMCU[] support top-down storage format 
 *            only, and all pointers pDstMCU[0..2] must be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - one or more of the following bad argument 
 *              conditions was detected: 
 *    -   a pointer was NULL 
 *    -   the absolute value of srcStep was smaller than 24 for YCbCr 444,
 *        or 48 for YCbCr422/ YCbCr420 
 *    -   the start address of each pointer in pDstMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_BGR888ToYCbCr420LS_MCU_U8_S16_C3P3 (
    const OMX_U8 *pSrc,
    OMX_INT srcStep,
    OMX_S16 *pDstMCU[3]
);



/**
 * Function:  omxIPCS_BGR565ToYCbCr444LS_MCU_U16_S16_C3P3   (4.4.3.7.6)
 *
 * Description:
 * This function converts packed BGR565 image data to the following 
 * sub-sampled color spaces:  YCbCr4:4:4, YCbCr4:2:2, and YCbCr4:2:0.  The 
 * parameter pDstMCU[0] must point to the start of a buffer with sufficient 
 * free space to contain the particular number of contiguously stored 8x8 Y 
 * blocks that constitute a complete MCU given the output chroma sub-sampling 
 * scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, or 4:2:0 
 * output, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrc - references the source image data buffer.  Pixel intensities are 
 *            interleaved as shown in Table 4 1, and G, B, and R are 
 *            represented using, respectively, 6, 5, and 5 bits.  The image 
 *            data buffer pSrcsupports bottom-up storage format, for which 
 *            srcStep can be less than 0. 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image; can be less than 0 to support bottom-up 
 *            storage format. 
 *
 * Output Arguments:
 *   
 *   pDstMCU[3] - output MCU pointers.  The parameter pDstMCU[0] must point 
 *            to the start of a buffer with sufficient free space to contain 
 *            the particular number of contiguously stored 8x8 Y blocks that 
 *            constitute a complete MCU given the output chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, 
 *            or 4:2:0 output, respectively. The parameter pDstMCU[1] points to 
 *            the Cb block, and the parameter pDstMCU[2] points to the Cr 
 *            block.  Output components are expressed using a Q8 
 *            representation and are bounded on the interval [-128, 127].  The 
 *            buffers referenced by pDstMCU[] support top-down storage format 
 *            only, and all pointers pDstMCU[0..2] must be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments. Returned for any of the following 
 *              conditions: 
 *    -   a pointer was NULL 
 *    -   srcStep was an odd number or its absolute value was less 
 *        than 16 for YCbCr444 or 32 for YCbCr422/YCbCr420. 
 *    -   a pointer in pDstMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_BGR565ToYCbCr444LS_MCU_U16_S16_C3P3 (
    const OMX_U16 *pSrc,
    OMX_INT srcStep,
    OMX_S16 *pDstMCU[3]
);



/**
 * Function:  omxIPCS_BGR565ToYCbCr422LS_MCU_U16_S16_C3P3   (4.4.3.7.6)
  *
 * Description:
 * This function converts packed BGR565 image data to the following 
 * sub-sampled color spaces:  YCbCr4:4:4, YCbCr4:2:2, and YCbCr4:2:0.  The 
 * parameter pDstMCU[0] must point to the start of a buffer with sufficient 
 * free space to contain the particular number of contiguously stored 8x8 Y 
 * blocks that constitute a complete MCU given the output chroma sub-sampling 
 * scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, or 4:2:0 
 * output, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrc - references the source image data buffer.  Pixel intensities are 
 *            interleaved as shown in Table 4 1, and G, B, and R are 
 *            represented using, respectively, 6, 5, and 5 bits.  The image 
 *            data buffer pSrcsupports bottom-up storage format, for which 
 *            srcStep can be less than 0. 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image; can be less than 0 to support bottom-up 
 *            storage format. 
 *
 * Output Arguments:
 *   
 *   pDstMCU[3] - output MCU pointers.  The parameter pDstMCU[0] must point 
 *            to the start of a buffer with sufficient free space to contain 
 *            the particular number of contiguously stored 8x8 Y blocks that 
 *            constitute a complete MCU given the output chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, 
 *            or 4:2:0 output, respectively. The parameter pDstMCU[1] points to 
 *            the Cb block, and the parameter pDstMCU[2] points to the Cr 
 *            block.  Output components are expressed using a Q8 
 *            representation and are bounded on the interval [-128, 127].  The 
 *            buffers referenced by pDstMCU[] support top-down storage format 
 *            only, and all pointers pDstMCU[0..2] must be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments. Returned for any of the following 
 *              conditions: 
 *    -   a pointer was NULL 
 *    -   srcStep was an odd number or its absolute value was less 
 *        than 16 for YCbCr444 or 32 for YCbCr422/YCbCr420. 
 *    -   a pointer in pDstMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_BGR565ToYCbCr422LS_MCU_U16_S16_C3P3 (
    const OMX_U16 *pSrc,
    OMX_INT srcStep,
    OMX_S16 *pDstMCU[3]
);



/**
 * Function:  omxIPCS_BGR565ToYCbCr420LS_MCU_U16_S16_C3P3   (4.4.3.7.6)
 *
 * Description:
 * This function converts packed BGR565 image data to the following 
 * sub-sampled color spaces:  YCbCr4:4:4, YCbCr4:2:2, and YCbCr4:2:0.  The 
 * parameter pDstMCU[0] must point to the start of a buffer with sufficient 
 * free space to contain the particular number of contiguously stored 8x8 Y 
 * blocks that constitute a complete MCU given the output chroma sub-sampling 
 * scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, or 4:2:0 
 * output, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrc - references the source image data buffer.  Pixel intensities are 
 *            interleaved as shown in Table 4 1, and G, B, and R are 
 *            represented using, respectively, 6, 5, and 5 bits.  The image 
 *            data buffer pSrcsupports bottom-up storage format, for which 
 *            srcStep can be less than 0. 
 *   srcStep - distance in bytes between the starts of consecutive lines in 
 *            the source image; can be less than 0 to support bottom-up 
 *            storage format. 
 *
 * Output Arguments:
 *   
 *   pDstMCU[3] - output MCU pointers.  The parameter pDstMCU[0] must point 
 *            to the start of a buffer with sufficient free space to contain 
 *            the particular number of contiguously stored 8x8 Y blocks that 
 *            constitute a complete MCU given the output chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 of 8x8 Y blocks for YCbCr 4:4:4, 4:2:2, 
 *            or 4:2:0 output, respectively. The parameter pDstMCU[1] points to 
 *            the Cb block, and the parameter pDstMCU[2] points to the Cr 
 *            block.  Output components are expressed using a Q8 
 *            representation and are bounded on the interval [-128, 127].  The 
 *            buffers referenced by pDstMCU[] support top-down storage format 
 *            only, and all pointers pDstMCU[0..2] must be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments. Returned for any of the following 
 *              conditions: 
 *    -   a pointer was NULL 
 *    -   srcStep was an odd number or its absolute value was less 
 *        than 16 for YCbCr444 or 32 for YCbCr422/YCbCr420. 
 *    -   a pointer in pDstMCU[] was not 8-byte aligned 
 *
 */
 OMXResult omxIPCS_BGR565ToYCbCr420LS_MCU_U16_S16_C3P3 (
    const OMX_U16 *pSrc,
    OMX_INT srcStep,
    OMX_S16 *pDstMCU[3]
);



/**
 * Function:  omxIPCS_YCbCr444ToBGR888LS_MCU_S16_U8_P3C3   (4.4.3.8.3)
 *
 * Description:
 * These functions convert sub-sampled YCbCr data to BGR888 data with 
 * level-shift.  The parameter pSrcMCU[0] must point to the start of a 
 * sufficient number of contiguously stored 8x8 Y blocks to constitute a 
 * complete MCU given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 * blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrcMCU - buffer containing input MCU pointers:  The parameter 
 *            pSrcMCU[0] points to the first Y block, pSrcMCU[1] points to Cb 
 *            the block, and pSrcMCU[2] points to the Cr block; all three 
 *            pointers must be aligned on 8-byte boundaries.  The parameter 
 *            pSrcMCU[0] must point to the start of a sufficient number of 
 *            contiguously stored 8x8 Y blocks to constitute a complete MCU 
 *            given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 *            blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively.  
 *            Only top-down storage format is supported.  Input components are 
 *            expressed using a Q8 representation and are bounded on the 
 *            interval [-128, 127]. 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image; values less than 0 are allowed to support 
 *            bottom-up storage format. 
 *
 * Output Arguments:
 *   
 *   pDst - points to the output image buffer in which the output data (C3 
 *            representation) are interleaved as follows:  BGRBGRBGR...  
 *            Bottom-up storage format is supported. Outputs are saturated to 
 *            the OMX_U8 range [0, 255].  The parameter dstStep can take 
 *            negative values to support bottom-up storage format. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments.  Returned if one or more of the 
 *              following was true: 
 *    -   a pointer was NULL 
 *    -   the absolute value of dstStep was smaller than 24 (for YCbCr444) 
 *        or 48 (for YCbCr422/YCbCr420) 
 *    -   a pointer in pSrcMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_YCbCr444ToBGR888LS_MCU_S16_U8_P3C3 (
    const OMX_S16 *pSrcMCU[3],
    OMX_U8 *pDst,
    OMX_INT dstStep
);



/**
 * Function:  omxIPCS_YCbCr422ToBGR888LS_MCU_S16_U8_P3C3   (4.4.3.8.3)
 *
 * Description:
 * These functions convert sub-sampled YCbCr data to BGR888 data with 
 * level-shift.  The parameter pSrcMCU[0] must point to the start of a 
 * sufficient number of contiguously stored 8x8 Y blocks to constitute a 
 * complete MCU given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 * blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrcMCU - buffer containing input MCU pointers:  The parameter 
 *            pSrcMCU[0] points to the first Y block, pSrcMCU[1] points to Cb 
 *            the block, and pSrcMCU[2] points to the Cr block; all three 
 *            pointers must be aligned on 8-byte boundaries.  The parameter 
 *            pSrcMCU[0] must point to the start of a sufficient number of 
 *            contiguously stored 8x8 Y blocks to constitute a complete MCU 
 *            given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 *            blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively.  
 *            Only top-down storage format is supported.  Input components are 
 *            expressed using a Q8 representation and are bounded on the 
 *            interval [-128, 127]. 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image; values less than 0 are allowed to support 
 *            bottom-up storage format. 
 *
 * Output Arguments:
 *   
 *   pDst - points to the output image buffer in which the output data (C3 
 *            representation) are interleaved as follows:  BGRBGRBGR...  
 *            Bottom-up storage format is supported. Outputs are saturated to 
 *            the OMX_U8 range [0, 255].  The parameter dstStep can take 
 *            negative values to support bottom-up storage format. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments.  Returned if one or more of the 
 *              following was true: 
 *    -   a pointer was NULL 
 *    -   the absolute value of dstStep was smaller than 24 (for YCbCr444) 
 *        or 48 (for YCbCr422/YCbCr420) 
 *    -   a pointer in pSrcMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_YCbCr422ToBGR888LS_MCU_S16_U8_P3C3 (
    const OMX_S16 *pSrcMCU[3],
    OMX_U8 *pDst,
    OMX_INT dstStep
);



/**
 * Function:  omxIPCS_YCbCr420ToBGR888LS_MCU_S16_U8_P3C3   (4.4.3.8.3)
 *
 * Description:
 * These functions convert sub-sampled YCbCr data to BGR888 data with 
 * level-shift.  The parameter pSrcMCU[0] must point to the start of a 
 * sufficient number of contiguously stored 8x8 Y blocks to constitute a 
 * complete MCU given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 * blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrcMCU - buffer containing input MCU pointers:  The parameter 
 *            pSrcMCU[0] points to the first Y block, pSrcMCU[1] points to Cb 
 *            the block, and pSrcMCU[2] points to the Cr block; all three 
 *            pointers must be aligned on 8-byte boundaries.  The parameter 
 *            pSrcMCU[0] must point to the start of a sufficient number of 
 *            contiguously stored 8x8 Y blocks to constitute a complete MCU 
 *            given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 *            blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively.  
 *            Only top-down storage format is supported.  Input components are 
 *            expressed using a Q8 representation and are bounded on the 
 *            interval [-128, 127]. 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image; values less than 0 are allowed to support 
 *            bottom-up storage format. 
 *
 * Output Arguments:
 *   
 *   pDst - points to the output image buffer in which the output data (C3 
 *            representation) are interleaved as follows:  BGRBGRBGR...  
 *            Bottom-up storage format is supported. Outputs are saturated to 
 *            the OMX_U8 range [0, 255].  The parameter dstStep can take 
 *            negative values to support bottom-up storage format. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments.  Returned if one or more of the 
 *              following was true: 
 *    -   a pointer was NULL 
 *    -   the absolute value of dstStep was smaller than 24 (for YCbCr444) 
 *        or 48 (for YCbCr422/YCbCr420) 
 *    -   a pointer in pSrcMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_YCbCr420ToBGR888LS_MCU_S16_U8_P3C3 (
    const OMX_S16 *pSrcMCU[3],
    OMX_U8 *pDst,
    OMX_INT dstStep
);



/**
 * Function:  omxIPCS_YCbCr444ToBGR565LS_MCU_S16_U16_P3C3   (4.4.3.8.6)
 *
 * Description:
 * These functions convert sub-sampled YCbCr data to BGR565 data with 
 * level-shift.  The parameter pSrcMCU[0] must point to the start of a 
 * sufficient number of contiguously stored 8x8 Y blocks to constitute a 
 * complete MCU given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 * blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrcMCU - buffer containing input MCU pointers: pSrcMCU[0] points to the 
 *            first Y block, pSrcMCU[1] points to Cb the block, and pSrcMCU[2] 
 *            points to the Cr block; all three must be aligned on 8-byte 
 *            boundaries.  The parameter pSrcMCU[0] must point to the start of 
 *            a sufficient number of contiguously stored 8x8 Y blocks to 
 *            constitute a complete MCU given the source chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 
 *            4:2:0, respectively. Only top-down storage format is supported.  
 *            Input components are represented using Q8 and are bounded on the 
 *            interval [-128, 127]. 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image; can take negative values to support 
 *            bottom-up storage format. 
 *
 * Output Arguments:
 *   
 *   pDst - output image buffer pointer; data are interleaved as follows:  
 *            [B G R B G R  ], where G is represented using 6 bits, and B and R 
 *            are represented using 5 bits.  Output components are saturated. 
 *            The parameter dstStep can take negative values to support 
 *            bottom-up storage. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments - returned if one or more of the 
 *              following is true: 
 *    -   a pointer was NULL 
 *    -   the absolute value of dstStep was smaller than 16 (for YCbCr444) 
 *        or 32 (for YCbCr422/YCbCr420) 
 *    -   a pointer in pSrcMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_YCbCr444ToBGR565LS_MCU_S16_U16_P3C3 (
    const OMX_S16 *pSrcMCU[3],
    OMX_U16 *pDst,
    OMX_INT dstStep
);



/**
 * Function:  omxIPCS_YCbCr422ToBGR565LS_MCU_S16_U16_P3C3   (4.4.3.8.6)
 *
 * Description:
 * These functions convert sub-sampled YCbCr data to BGR565 data with 
 * level-shift.  The parameter pSrcMCU[0] must point to the start of a 
 * sufficient number of contiguously stored 8x8 Y blocks to constitute a 
 * complete MCU given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 * blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrcMCU - buffer containing input MCU pointers: pSrcMCU[0] points to the 
 *            first Y block, pSrcMCU[1] points to Cb the block, and pSrcMCU[2] 
 *            points to the Cr block; all three must be aligned on 8-byte 
 *            boundaries.  The parameter pSrcMCU[0] must point to the start of 
 *            a sufficient number of contiguously stored 8x8 Y blocks to 
 *            constitute a complete MCU given the source chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 
 *            4:2:0, respectively. Only top-down storage format is supported.  
 *            Input components are represented using Q8 and are bounded on the 
 *            interval [-128, 127]. 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image; can take negative values to support 
 *            bottom-up storage format. 
 *
 * Output Arguments:
 *   
 *   pDst - output image buffer pointer; data are interleaved as follows:  
 *            [B G R B G R  ], where G is represented using 6 bits, and B and R 
 *            are represented using 5 bits.  Output components are saturated. 
 *            The parameter dstStep can take negative values to support 
 *            bottom-up storage. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments - returned if one or more of the 
 *              following is true: 
 *    -   a pointer was NULL 
 *    -   the absolute value of dstStep was smaller than 16 (for YCbCr444) 
 *        or 32 (for YCbCr422/YCbCr420) 
 *    -   a pointer in pSrcMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_YCbCr422ToBGR565LS_MCU_S16_U16_P3C3 (
    const OMX_S16 *pSrcMCU[3],
    OMX_U16 *pDst,
    OMX_INT dstStep
);



/**
 * Function:  omxIPCS_YCbCr420ToBGR565LS_MCU_S16_U16_P3C3   (4.4.3.8.6)
 *
 * Description:
 * These functions convert sub-sampled YCbCr data to BGR565 data with 
 * level-shift.  The parameter pSrcMCU[0] must point to the start of a 
 * sufficient number of contiguously stored 8x8 Y blocks to constitute a 
 * complete MCU given the source chroma sub-sampling scheme, i.e., 1, 2, or 4 
 * blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 4:2:0, respectively. 
 *
 * Input Arguments:
 *   
 *   pSrcMCU - buffer containing input MCU pointers: pSrcMCU[0] points to the 
 *            first Y block, pSrcMCU[1] points to Cb the block, and pSrcMCU[2] 
 *            points to the Cr block; all three must be aligned on 8-byte 
 *            boundaries.  The parameter pSrcMCU[0] must point to the start of 
 *            a sufficient number of contiguously stored 8x8 Y blocks to 
 *            constitute a complete MCU given the source chroma sub-sampling 
 *            scheme, i.e., 1, 2, or 4 blocks for YCbCr 4:4:4, 4:2:2, or YCbCr 
 *            4:2:0, respectively. Only top-down storage format is supported.  
 *            Input components are represented using Q8 and are bounded on the 
 *            interval [-128, 127]. 
 *   dstStep - distance in bytes between the starts of consecutive lines in 
 *            the destination image; can take negative values to support 
 *            bottom-up storage format. 
 *
 * Output Arguments:
 *   
 *   pDst - output image buffer pointer; data are interleaved as follows:  
 *            [B G R B G R  ], where G is represented using 6 bits, and B and R 
 *            are represented using 5 bits.  Output components are saturated. 
 *            The parameter dstStep can take negative values to support 
 *            bottom-up storage. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments - returned if one or more of the 
 *              following is true: 
 *    -   a pointer was NULL 
 *    -   the absolute value of dstStep was smaller than 16 (for YCbCr444) 
 *        or 32 (for YCbCr422/YCbCr420) 
 *    -   a pointer in pSrcMCU[] was not 8-byte aligned 
 *
 */
OMXResult omxIPCS_YCbCr420ToBGR565LS_MCU_S16_U16_P3C3 (
    const OMX_S16 *pSrcMCU[3],
    OMX_U16 *pDst,
    OMX_INT dstStep
);



#ifdef __cplusplus
}
#endif

#endif /** end of #define _OMXIP_H_ */

/** EOF */

