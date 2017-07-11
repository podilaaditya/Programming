/**
 * File: omxAC.h
 * Brief: OpenMAX DL v1.0.2 - Audio Coding library
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

#ifndef _OMXAC_H_
#define _OMXAC_H_

#include "omxtypes.h"

#ifdef __cplusplus
extern "C" {
#endif


/* Table 3-1: MP3 Macro and Constant Definitions */
#define OMX_MP3_GRANULE_LEN 576  /** The number of samples in one granule */
#define OMX_MP3_SF_BUF_LEN 40    /** Scalefactor buffer length (8-bit words) */
#define OMX_MP3_V_BUF_LEN 512    /** V data buffer length (32-bit words) */
#define OMX_MP3_SFB_TABLE_LONG_LEN 138
#define OMX_MP3_SFB_TABLE_SHORT_LEN 84
#define OMX_MP3_SFB_MBP_TABLE_LEN 12

 typedef const OMX_S16 OMXMP3ScaleFactorBandTableLong[OMX_MP3_SFB_TABLE_LONG_LEN]; /* 138 elements */
 typedef const OMX_S16 OMXMP3ScaleFactorBandTableShort[OMX_MP3_SFB_TABLE_SHORT_LEN]; /* 84 elements */
 typedef const OMX_S16 OMXMP3MixedBlockPartitionTable[OMX_MP3_SFB_MBP_TABLE_LEN];


/* Table 3-7: AAC-LC/LTP Constants */
#define OMX_AAC_FRAME_LEN 1024   /* The number of data in one frame */
#define OMX_AAC_SF_LEN 120       /* scalefactor buffer length */
#define OMX_AAC_GROUP_NUM_MAX 8  /* maximum group number for one frame */
#define OMX_AAC_TNS_COEF_LEN 60  /* TNS coefficients buffer length */
#define OMX_AAC_TNS_FILT_MAX 8   /* maximum TNS filters for one frame */
#define OMX_AAC_PRED_SFB_MAX 41  /* maximum prediction scalefactor bands for one frame */
#define OMX_AAC_ELT_NUM 16       /* maximum number of elements for one program.  */
#define OMX_AAC_LFE_ELT_NUM 4    /* maximum Low Frequency Enhance elements number for one program */
#define OMX_AAC_DATA_ELT_NUM 8   /* maximum data elements number for one program */
#define OMX_AAC_COMMENTS_LEN 256 /* maximum length of the comment field, in bytes. */
#define OMX_AAC_SF_MAX 60        /* maximum number of scalefactor bands in one window */
#define OMX_AAC_WIN_MAX 8 
#define OMX_AAC_MAX_LTP_SFB 40 


/* 3.1.2.1 Frame Header  */

typedef struct {
    OMX_INT idEx;   /* idEx/id: 1/1 = MPEG-1,  */
    OMX_INT id;     /* idEx/id: 1/0 = MPEG-2,  */
                    /* idEx/id: 0/0 = MPEG-2.5 */  
    
    OMX_INT layer; /** layer index 0x3: Layer I  
                     *             0x2: Layer II  
                     *             0x1: Layer III  
                     */
       
    OMX_INT protectionBit; /** CRC flag 0: CRC on, 1: CRC off */
    OMX_INT bitRate;       /** bit rate index */
    OMX_INT samplingFreq;  /** sampling frequency index */
    OMX_INT paddingBit;    /** padding flag 0: no padding, 1 padding  */
    OMX_INT privateBit;    /** private_bit, no use */
    OMX_INT mode;          /** mono/stereo select information */
    OMX_INT modeExt;       /** extension to mode */
    OMX_INT copyright;     /** copyright or not, 0: no, 1: yes */
    OMX_INT originalCopy;  /** original bitstream or copy, 0: copy, 1:  original */
    OMX_INT emphasis;      /** flag indicates the type of de-emphasis  that shall be used */
    OMX_INT CRCWord;       /** CRC-check word */
} OMXMP3FrameHeader;



/* 3.1.2.2 Side Information   */

typedef struct {
    OMX_INT part23Len;  /* number of main_data bits */
    OMX_INT bigVals;    /* half the number of values in the  “bigvalues” section, 
                         * regions 0-2, i.e.,  half the number of Huffman code 
                         * words  whose maximum amplitudes may exceed 1. */
    OMX_INT globGain;   /* quantizer step size */
    OMX_INT sfCompress; /* number of bits used for scale factors */
    OMX_INT winSwitch;  /* window switching flag */
    OMX_INT blockType;  /* block type flag */
    OMX_INT mixedBlock; /* flag 0: non-mixed block, 1: mixed block */
    OMX_INT pTableSelect[3]; /* Huffman table indices for each of the 3  regions: region0, region1, and region2. */
    OMX_INT pSubBlkGain[3];  /* gain offset from the global gain for onesubblock */
    OMX_INT reg0Cnt;    /* the number of scale factor bands in the first  region less one */
    OMX_INT reg1Cnt;    /* the number of scale factor bands in the second  region less one */
    OMX_INT preFlag;    /* flag indicating high frequency boost */
    OMX_INT sfScale;    /* scalefactor scaling */
    OMX_INT cnt1TabSel; /* Huffman table index for the quadruples */
} OMXMP3SideInfo;



/**
 * Function:  omxACMP3_UnpackFrameHeader   (3.1.3.1.1)
 *
 * Description:
 * Unpacks the audio frame header. If CRC is enabled, this function also 
 * unpacks the CRC word. Before calling omxACMP3_UnpackFrameHeader, the 
 * decoder application should locate the bit stream sync word and ensure that 
 * *ppBitStream points to the first byte of the 32-bit frame header. If CRC is 
 * enabled, it is assumed that the 16-bit CRC word is adjacent to the 32-bit 
 * frame header, as defined in the MP3 standard. Before returning to the 
 * caller, the function updates the pointer *ppBitStream, such that it 
 * references the next byte after the frame header or the CRC word. The first 
 * byte of the 16-bit CRC word is stored in pFrameHeader->CRCWord[15:8], and 
 * the second byte is stored in pFrameHeader->CRCWord[7:0]. The function does 
 * not detect corrupted frame headers. Reference [ISO13818-3], sub-clause 
 * 2.4.2.3 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the first byte of the MP3 frame header 
 *
 * Output Arguments:
 *   
 *   pFrameHeader - pointer to the MP3 frame header structure (defined in 
 *            section  Data Structures ) 
 *   ppBitStream - double pointer to the byte immediately following the frame 
 *            header 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - invalid arguments - either ppBitStream, 
 *              pFrameHeader, or *ppBitStream is NULL 
 *
 */
OMXResult omxACMP3_UnpackFrameHeader (
    const OMX_U8 **ppBitStream,
    OMXMP3FrameHeader *pFrameHeader
);



/**
 * Function:  omxACMP3_UnpackSideInfo   (3.1.3.1.2)
 *
 * Description:
 * Unpacks the side information from the input bit stream. Before 
 * omxACMP3_UnpackSideInfo is called, the pointer *ppBitStream must point to 
 * the first byte of the bit stream that contains the side information 
 * associated with the current frame. Before returning to the caller, the 
 * function updates the pointer *ppBitStream such that it references the next 
 * byte after the side information. References 1. [ISO13818-3], sub-clause 
 * 2.4.1.7 2. [ISO11172-3], sub-clauses 2.4.2.7 and 2.4.3.4.10.3 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the first byte of the side information 
 *            associated with the current frame in the bit stream buffer 
 *   pFrameHeader - pointer to the structure that contains the unpacked MP3 
 *            frame header. The header structure provides format information 
 *            about the input bit stream. Both single- and dual-channel MPEG-1 
 *            and MPEG-2 modes are supported. 
 *
 * Output Arguments:
 *   
 *   pDstSideInfo - array of pointers to the MP3 side information 
 *            structure(s). The structure(s) contain(s) side information that 
 *            applies to all granules and all channels for the current frame.  
 *            Depending on the number of channels and granules associated with 
 *            the current frame, one or more of the structures are placed 
 *            contiguously in the buffer pointed by pDstSideInfo in the 
 *            following order: { granule 0 (channel 0, channel 1), granule 1 
 *            (channel 0, channel 1) }. 
 *   pDstMainDataBegin - pointer to the main_data_begin field 
 *   pDstPrivateBits - pointer to the private bits field 
 *   pDstScfsi - pointer to the scalefactor selection information associated 
 *            with the current frame, organized contiguously in the buffer 
 *            pointed to by pDstScfsi in the following order: {channel 0 
 *            (scfsi_band 0, scfsi_band 1, ..., scfsi_band 3), channel 1 
 *            (scfsi_band 0, scfsi_band 1, ..., scfsi_band 3) }. 
 *   ppBitStream - double pointer to the bit stream buffer byte immediately 
 *            following the side information for the current frame 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad argument; at least one of the following 
 *              pointers is NULL: ppBitStream pDstSideInfo pDstMainDataBegin 
 *              pDstPrivateBits pDstScfsi pFrameHeader 
 *    -   *ppBitStream 
 *    OMX_Sts_Err - one or more elements of the MP3 frame header structure is 
 *              invalid, i.e., one or more of the following conditions is 
 *              true: 
 *                 - pFrameHeader->id exceeds [0,1]; 
 *                 - pFrameHeader->layer!=1 
 *                 - pFrameHeader->mode exceeds [0,3] 
 *                 - pDstSideInfo[]->blockType==0 ( normal ) 
                     and pDstSideInfo[] >winSwitch==1 ( set ). 
 *
 */
OMXResult omxACMP3_UnpackSideInfo (
    const OMX_U8 **ppBitStream,
    OMXMP3SideInfo *pDstSideInfo,
    OMX_INT *pDstMainDataBegin,
    OMX_INT *pDstPrivateBits,
    OMX_INT *pDstScfsi,
    OMXMP3FrameHeader *pFrameHeader
);



/**
 * Function:  omxACMP3_UnpackScaleFactors_S8   (3.1.3.1.3)
 *
 * Description:
 * Unpacks short and/or long block scalefactors for one granule of one 
 * channel and places the results in the vector pDstScaleFactor. Special 
 * scalefactor values on the right channel of intensity-coded scalefactor 
 * bands are returned to indicate illegal intensity positions.  Before 
 * returning to the caller, the function updates *ppBitStream and *pOffset 
 * such that they point to the next available bit in the input bit stream. 
 * Reference [ISO13818-3], sub-clause 2.4.1.7 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the first bit stream buffer byte that is 
 *            associated with the scalefactors for the current frame, granule, 
 *            and channel 
 *   pOffset - pointer to the next bit in the byte referenced by 
 *            *ppBitStream. Valid within the range of 0 to 7, where 0 
 *            corresponds to the most significant bit and 7 corresponds to the 
 *            least significant bit. 
 *   pSideInfo - pointer to the MP3 side information structure associated 
 *            with the current granule and channel 
 *   pScfsi - pointer to scalefactor selection information for the current 
 *            channel 
 *   channel - channel index; can take on the values of either 0 or 1 
 *   granule - granule index; can take on the values of either 0 or 1 
 *   pFrameHeader - pointer to MP3 frame header structure for the current 
 *            frame 
 *
 * Output Arguments:
 *   
 *   pDstScaleFactor - pointer to the scalefactor vector for long and/or 
 *            short blocks.  Special scalefactor values on the right channel 
 *            of intensity-coded scalefactor bands shall be used to indicate 
 *            illegal intensity positions as follows:  
 *                   i) for MPEG-1 streams (pFrameHeader->id==1), 
                        the value 7 [ISO11172-3, section 2.4.3.4.9.3], and 
                    ii) for MPEG-2 streams (pFrameHeader->id==0), 
 *                      a negative value. 
 *   ppBitStream - updated double pointer to the next bit stream byte 
 *   pOffset - updated pointer to the next bit in the bit stream (indexes the 
 *            bits of the byte pointed to by *ppBitStream). Valid within the 
 *            range of 0 to 7, where 0 corresponds to the most significant bit 
 *            and 7 corresponds to the least significant bit. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments; one or more of the following 
 *              pointers is NULL: 
 *    -   *ppBitStream pOffset pDstScaleFactor pSideInfo, pScfsi ppBitStream 
 *              pFrameHeader Bad arguments are also flagged when the value of 
 *              *pOffset exceeds [0,7] or the granule or channel indices have 
 *              values other than 0 or 1. 
 *    OMX_Sts_Err - input data errors detected; one or more of the following 
 *              are true: 
 *            - pFrameHeader ->id exceeds [0,1], 
 *            - pSideInfo->blockType exceeds [0,3], 
 *            - pSideInfo->mixedBlock exceeds [0,1] when 
 *              pFrameHeader->id==1, pSideInfo->blockType==2, 
 *              and pSideInfo->winSwitch==1. 
 *            - pScfsi[0..3] exceeds [0,1] when pFrameHeader->id==1 and 
 *              either pSideInfo->blockType!=2 or pSideInfo->winSwitch!=1. 
 *            - pFrameHeader->id indicates that the bit stream is MPEG-1 and 
 *              pSideInfo->sfCompress exceeds [0,15] 
 *            - pFrameHeader->id indicates the bit stream is MPEG-2 and 
 *              pSideInfo->sfCompress exceeds [0,511] 
 *            - pFrameHeader->modeExt exceeds [0, 3] 
 *
 */
OMXResult omxACMP3_UnpackScaleFactors_S8 (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMX_S8 *pDstScaleFactor,
    OMXMP3SideInfo *pSideInfo,
    OMX_INT *pScfsi,
    OMXMP3FrameHeader *pFrameHeader,
    OMX_INT granule,
    OMX_INT channel
);



/**
 * Function:  omxACMP3_HuffmanDecode_S32   (3.1.3.2.3)
 *
 * Description:
 * Decodes Huffman symbols for the 576 spectral coefficients associated with 
 * one granule of one channel. References 1. [ISO11172-3], Table B.8 
 * ( MPEG-1 ) 2. [ISO13818-3], sub-clause 2.5.2.8 and Table B.2 ( MPEG-2 ) 3. 
 * [ISO14496-3], sub-part 9,  MPEG-1/2 Audio in MPEG-4  
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the first bit stream byte that contains 
 *            the Huffman code words associated with the current granule and 
 *            channel 
 *   pOffset- pointer to the starting bit position in the bit stream byte 
 *            pointed by *ppBitStream; valid within the range of 0 to 7, where 
 *            0 corresponds to the most significant bit, and 7 corresponds to 
 *            the least significant bit 
 *   pSideInfo - pointer to MP3 structure that contains the side information 
 *            associated with the current granule and channel 
 *   pFrameHeader - pointer to MP3 structure that contains the header 
 *            associated with the current frame 
 *   hufSize - the number of Huffman code bits associated with the current 
 *            granule and channel 
 *   pSfbTableLong - pointer to the long block scalefactor band table, 
 *            formatted as described in section 3.1.2.3.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-1. Alternatively the table 
 *            entries may be defined to suit a special purpose. References:  
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *   pSfbTableShort - pointer to the short block scalefactor band table, 
 *            formatted as described in section 3.1.2.4.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-2.  Alternatively the table 
 *            entries may be defined to suit a special purpose.  References: 
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *   pMbpTable - pointer to the mixed block partitioning table, formatted as 
 *            described in section 3.1.2.5. Table entries optionally may 
 *            follow the MPEG-1, MPEG-2, or MPEG-4 audio standards as shown in 
 *            Example 3-3. Alternatively the table entries may be defined to 
 *            suit a special purpose.  References:  [ISO11172-3], Table B.8, 
 *            [ISO13818-3], Table B.2, [ISO14496-3], sub-part 9. 
 *
 * Output Arguments:
 *   
 *   pDstIs - pointer to the vector of decoded Huffman symbols used to 
 *            compute the quantized values of the 576 spectral coefficients 
 *            that are associated with the current granule and channel 
 *   pDstNonZeroBound - pointer to the spectral region above which all 
 *            coefficients are set equal to zero 
 *   ppBitStream - updated double pointer to the particular byte in the bit 
 *            stream that contains the first new bit following the decoded 
 *            block of Huffman codes 
 *   pOffset - updated pointer to the next bit position in the byte pointed 
 *            by *ppBitStream; valid within the range of 0 to 7, where 0 
 *            corresponds to the most significant bit, and 7 corresponds to 
 *            the least significant bit 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments detected; at least one of the 
 *              following pointers is NULL: 
 *            - ppBitStream 
 *            - pOffset 
 *            - pDstIs 
 *            - pDstNonZeroBound
 *            - pSideInfo 
 *            - pFrameHeader 
 *            - pSfbTableLong 
 *            - *ppBitStream 
 *        Or any one or more of the following conditions are true: 
 *            - *pOffset < 0, or *pOffset > 7. 
 *            - hufSize < 0 or hufSize > pSideInfo->part23Len 
 *            - pSideInfo-> bigVals * 2 > OMX_MP3_GRANULE_LEN 
 *            - pSideInfo->bigVals * 2 < 0 
 *            - pSideInfo->winSwitch outside the range [0,1] 
 *            - pSideInfo->blockType outside the range [0,3] 
 *            - pSideInfo->winSwitch==1 when 
 *              pSideInfo->blockType==0 
 *            - pSideInfo->cnt1TabSel outside the range [0,1] 
 *            - pSideInfo-> reg0Cnt < 0 when pSideInfo->blockType==0 
 *            - pSideInfo-> reg1Cnt < 0 when pSideInfo->blockType==0 
 *            - pSideInfo-> reg0Cnt + pSideInfo -> reg1Cnt + 2 > 22 
 *              when pSideInfo->blockType==0 
 *            - pSideInfo-> pTableSelect[0] outside the range [0,31]  
 *            - pSideInfo-> pTableSelect[1] outside the range [0,31] 
 *            - pSideInfo-> pTableSelect[2] outside the range [0,31]
 *              when pSideInfo->blockType==0 
 *            - pFrameHeader-> id outside the range [0,1] 
 *            - pFrameHeader-> idEX outside the range [0,1]
 *            - pFrameHeader-> idEX == 0 when pFrameHeader->id==1 
 *            - pFrameHeader->layer !=1 
 *            - pFrameHeader->samplingFreq outside the range [0,2] 
 *            - hufSize outside the range [0, pSideInfo->part23Len] 
 *    OMX_Sts_Err - indicates that the number of remaining Huffman code bits 
 *              for <count1> partition is less than zero after decoding the 
 *              <big_values> partition. 
 *
 */
OMXResult omxACMP3_HuffmanDecode_S32 (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMX_S32 *pDstIs,
    OMX_INT *pDstNonZeroBound,
    OMXMP3SideInfo*pSideInfo,
    OMXMP3FrameHeader *pFrameHeader,
    OMX_INT hufSize
);



/**
 * Function:  omxACMP3_HuffmanDecodeSfb_S32   (3.1.3.2.3)
 *
 * Description:
 * Decodes Huffman symbols for the 576 spectral coefficients associated with 
 * one granule of one channel. References 1. [ISO11172-3], Table B.8 
 * ( MPEG-1 ) 2. [ISO13818-3], sub-clause 2.5.2.8 and Table B.2 ( MPEG-2 ) 3. 
 * [ISO14496-3], sub-part 9,  MPEG-1/2 Audio in MPEG-4  
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the first bit stream byte that contains 
 *            the Huffman code words associated with the current granule and 
 *            channel 
 *   pOffset- pointer to the starting bit position in the bit stream byte 
 *            pointed by *ppBitStream; valid within the range of 0 to 7, where 
 *            0 corresponds to the most significant bit, and 7 corresponds to 
 *            the least significant bit 
 *   pSideInfo - pointer to MP3 structure that contains the side information 
 *            associated with the current granule and channel 
 *   pFrameHeader - pointer to MP3 structure that contains the header 
 *            associated with the current frame 
 *   hufSize - the number of Huffman code bits associated with the current 
 *            granule and channel 
 *   pSfbTableLong - pointer to the long block scalefactor band table, 
 *            formatted as described in section 3.1.2.3.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-1. Alternatively the table 
 *            entries may be defined to suit a special purpose. References:  
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *   pSfbTableShort - pointer to the short block scalefactor band table, 
 *            formatted as described in section 3.1.2.4.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-2.  Alternatively the table 
 *            entries may be defined to suit a special purpose.  References: 
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *   pMbpTable - pointer to the mixed block partitioning table, formatted as 
 *            described in section 3.1.2.5. Table entries optionally may 
 *            follow the MPEG-1, MPEG-2, or MPEG-4 audio standards as shown in 
 *            Example 3-3. Alternatively the table entries may be defined to 
 *            suit a special purpose.  References:  [ISO11172-3], Table B.8, 
 *            [ISO13818-3], Table B.2, [ISO14496-3], sub-part 9. 
 *
 * Output Arguments:
 *   
 *   pDstIs - pointer to the vector of decoded Huffman symbols used to 
 *            compute the quantized values of the 576 spectral coefficients 
 *            that are associated with the current granule and channel 
 *   pDstNonZeroBound - pointer to the spectral region above which all 
 *            coefficients are set equal to zero 
 *   ppBitStream - updated double pointer to the particular byte in the bit 
 *            stream that contains the first new bit following the decoded 
 *            block of Huffman codes 
 *   pOffset - updated pointer to the next bit position in the byte pointed 
 *            by *ppBitStream; valid within the range of 0 to 7, where 0 
 *            corresponds to the most significant bit, and 7 corresponds to 
 *            the least significant bit 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments detected; at least one of the 
 *              following pointers is NULL: 
 *            - ppBitStream 
 *            - pOffset 
 *            - pDstIs 
 *            - pDstNonZeroBound
 *            - pSideInfo 
 *            - pFrameHeader 
 *            - pSfbTableLong 
 *            - *ppBitStream 
 *        Or any one or more of the following conditions are true: 
 *            - *pOffset < 0, or *pOffset > 7. 
 *            - hufSize < 0 or hufSize > pSideInfo->part23Len 
 *            - pSideInfo-> bigVals * 2 > OMX_MP3_GRANULE_LEN 
 *            - pSideInfo->bigVals * 2 < 0 
 *            - pSideInfo->winSwitch outside the range [0,1] 
 *            - pSideInfo->blockType outside the range [0,3] 
 *            - pSideInfo->winSwitch==1 when 
 *              pSideInfo->blockType==0 
 *            - pSideInfo->cnt1TabSel outside the range [0,1] 
 *            - pSideInfo-> reg0Cnt < 0 when pSideInfo->blockType==0 
 *            - pSideInfo-> reg1Cnt < 0 when pSideInfo->blockType==0 
 *            - pSideInfo-> reg0Cnt + pSideInfo -> reg1Cnt + 2 > 22 
 *              when pSideInfo->blockType==0 
 *            - pSideInfo-> pTableSelect[0] outside the range [0,31]  
 *            - pSideInfo-> pTableSelect[1] outside the range [0,31] 
 *            - pSideInfo-> pTableSelect[2] outside the range [0,31]
 *              when pSideInfo->blockType==0 
 *            - pFrameHeader-> id outside the range [0,1] 
 *            - pFrameHeader-> idEX outside the range [0,1]
 *            - pFrameHeader-> idEX == 0 when pFrameHeader->id==1 
 *            - pFrameHeader->layer !=1 
 *            - pFrameHeader->samplingFreq outside the range [0,2] 
 *            - hufSize outside the range [0, pSideInfo->part23Len] 
 *    OMX_Sts_Err - indicates that the number of remaining Huffman code bits 
 *              for <count1> partition is less than zero after decoding the 
 *              <big_values> partition. 
 *
 */
OMXResult omxACMP3_HuffmanDecodeSfb_S32 (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMX_S32 *pDstIs,
    OMX_INT *pDstNonZeroBound,
    OMXMP3SideInfo*pSideInfo,
    OMXMP3FrameHeader *pFrameHeader,
    OMX_INT hufSize,
    OMXMP3ScaleFactorBandTableLong pSfbTableLong
);



/**
 * Function:  omxACMP3_HuffmanDecodeSfbMbp_S32   (3.1.3.2.3)
 *
 * Description:
 * Decodes Huffman symbols for the 576 spectral coefficients associated with 
 * one granule of one channel. References 1. [ISO11172-3], Table B.8 
 * ( MPEG-1 ) 2. [ISO13818-3], sub-clause 2.5.2.8 and Table B.2 ( MPEG-2 ) 3. 
 * [ISO14496-3], sub-part 9,  MPEG-1/2 Audio in MPEG-4  
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the first bit stream byte that contains 
 *            the Huffman code words associated with the current granule and 
 *            channel 
 *   pOffset- pointer to the starting bit position in the bit stream byte 
 *            pointed by *ppBitStream; valid within the range of 0 to 7, where 
 *            0 corresponds to the most significant bit, and 7 corresponds to 
 *            the least significant bit 
 *   pSideInfo - pointer to MP3 structure that contains the side information 
 *            associated with the current granule and channel 
 *   pFrameHeader - pointer to MP3 structure that contains the header 
 *            associated with the current frame 
 *   hufSize - the number of Huffman code bits associated with the current 
 *            granule and channel 
 *   pSfbTableLong - pointer to the long block scalefactor band table, 
 *            formatted as described in section 3.1.2.3.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-1. Alternatively the table 
 *            entries may be defined to suit a special purpose. References:  
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *   pSfbTableShort - pointer to the short block scalefactor band table, 
 *            formatted as described in section 3.1.2.4.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-2.  Alternatively the table 
 *            entries may be defined to suit a special purpose.  References: 
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *   pMbpTable - pointer to the mixed block partitioning table, formatted as 
 *            described in section 3.1.2.5. Table entries optionally may 
 *            follow the MPEG-1, MPEG-2, or MPEG-4 audio standards as shown in 
 *            Example 3-3. Alternatively the table entries may be defined to 
 *            suit a special purpose.  References:  [ISO11172-3], Table B.8, 
 *            [ISO13818-3], Table B.2, [ISO14496-3], sub-part 9. 
 *
 * Output Arguments:
 *   
 *   pDstIs - pointer to the vector of decoded Huffman symbols used to 
 *            compute the quantized values of the 576 spectral coefficients 
 *            that are associated with the current granule and channel 
 *   pDstNonZeroBound - pointer to the spectral region above which all 
 *            coefficients are set equal to zero 
 *   ppBitStream - updated double pointer to the particular byte in the bit 
 *            stream that contains the first new bit following the decoded 
 *            block of Huffman codes 
 *   pOffset - updated pointer to the next bit position in the byte pointed 
 *            by *ppBitStream; valid within the range of 0 to 7, where 0 
 *            corresponds to the most significant bit, and 7 corresponds to 
 *            the least significant bit 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments detected; at least one of the 
 *              following pointers is NULL: 
 *            - ppBitStream 
 *            - pOffset 
 *            - pDstIs 
 *            - pDstNonZeroBound
 *            - pSideInfo 
 *            - pFrameHeader 
 *            - pSfbTableLong 
 *            - *ppBitStream 
 *        Or any one or more of the following conditions are true: 
 *            - *pOffset < 0, or *pOffset > 7. 
 *            - hufSize < 0 or hufSize > pSideInfo->part23Len 
 *            - pSideInfo-> bigVals * 2 > OMX_MP3_GRANULE_LEN 
 *            - pSideInfo->bigVals * 2 < 0 
 *            - pSideInfo->winSwitch outside the range [0,1] 
 *            - pSideInfo->blockType outside the range [0,3] 
 *            - pSideInfo->winSwitch==1 when 
 *              pSideInfo->blockType==0 
 *            - pSideInfo->cnt1TabSel outside the range [0,1] 
 *            - pSideInfo-> reg0Cnt < 0 when pSideInfo->blockType==0 
 *            - pSideInfo-> reg1Cnt < 0 when pSideInfo->blockType==0 
 *            - pSideInfo-> reg0Cnt + pSideInfo -> reg1Cnt + 2 > 22 
 *              when pSideInfo->blockType==0 
 *            - pSideInfo-> pTableSelect[0] outside the range [0,31]  
 *            - pSideInfo-> pTableSelect[1] outside the range [0,31] 
 *            - pSideInfo-> pTableSelect[2] outside the range [0,31]
 *              when pSideInfo->blockType==0 
 *            - pFrameHeader-> id outside the range [0,1] 
 *            - pFrameHeader-> idEX outside the range [0,1]
 *            - pFrameHeader-> idEX == 0 when pFrameHeader->id==1 
 *            - pFrameHeader->layer !=1 
 *            - pFrameHeader->samplingFreq outside the range [0,2] 
 *            - hufSize outside the range [0, pSideInfo->part23Len] 
 *    OMX_Sts_Err - indicates that the number of remaining Huffman code bits 
 *              for <count1> partition is less than zero after decoding the 
 *              <big_values> partition. 
 *
 */
OMXResult omxACMP3_HuffmanDecodeSfbMbp_S32 (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMX_S32 *pDstIs,
    OMX_INT *pDstNonZeroBound,
    OMXMP3SideInfo *pSideInfo,
    OMXMP3FrameHeader *pFrameHeader,
    OMX_INT hufSize,
    OMXMP3ScaleFactorBandTableLong pSfbTableLong,
    OMXMP3ScaleFactorBandTableShort pSfbTableShort,
    OMXMP3MixedBlockPartitionTable pMbpTable
);



/**
 * Function:  omxACMP3_ReQuantize_S32_I   (3.1.3.3.2)
 *
 * Description:
 * Requantizes the decoded Huffman symbols.  Spectral samples for the 
 * synthesis filter bank are derived from the decoded symbols using the 
 * requantization equations given in the ISO standard.  Stereophonic mid/side 
 * (M/S) and/or intensity decoding is applied if necessary.  Requantized 
 * spectral samples are returned in the vector pSrcDstIsXr. The reordering 
 * operation is applied for short blocks.  Users must preallocate a workspace 
 * buffer pointed to by pBuffer prior to calling the requantization function. 
 * The value pointed by pNonZeroBound will be recalculated according to the 
 * output data sequence.  Special scalefactor values on the right channel of 
 * intensity-coded scalefactor bands are used to indicate illegal intensity 
 * positions. References 1. [ISO11172-3], Table B.8 ( MPEG-1 ) 2. 
 * [ISO13818-3], sub-clause 2.5.3.2.2 and Table B.2 ( MPEG-2 ) 3. 
 * [ISO14496-3], sub-part 9,  MPEG-1/2 Audio in MPEG-4  
 *
 * Input Arguments:
 *   
 *   pSrcDstIsXr - pointer to the vector of decoded Huffman symbols; for 
 *            stereo and dual-channel modes, right channel data begins at the 
 *            address &(pSrcDstIsXr[576]). This pointer must be aligned on an 
 *            8-byte boundary. 
 *   pNonZeroBound - (Inout/output argument) pointer to the spectral bound 
 *            above which all coefficients are set to zero; for stereo and 
 *            dual-channel modes, the left channel bound is pNonZeroBound [0], 
 *            and the right channel bound is pNonZeroBound [1] 
 *   pScaleFactor - pointer to the scalefactor buffer; for stereo and 
 *            dual-channel modes, the right channel scalefactors begin at 
 *            &(pScaleFactor[OMX_MP3_SF_BUF_LEN]). Special scalefactor values 
 *            on the right channel of intensity-coded scalefactor bands shall 
 *            be used to indicate an illegal intensity position as follows: i) 
 *            for MPEG-1 streams (pFrameHeader->id==1), the value 7 
 *            [ISO11172-3, section 2.4.3.4.9.3], and ii) for MPEG-2 streams 
 *            (pFrameHeader->id==0), a negative value. 
 *   pSideInfo - pointer to the side information for the current granule 
 *   pFrameHeader - pointer to the frame header for the current frame 
 *   pBuffer - pointer to a workspace buffer. The buffer length must be 576 
 *            samples, and the pointer must be 8-byte aligned. 
 *   pSfbTableLong - pointer to the long block scalefactor band table, 
 *            formatted as described in section 3.1.2.3.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-1. Alternatively the table 
 *            entries may be defined to suit a special purpose. References:  
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *   pSfbTableShort - pointer to the short block scalefactor band table, 
 *            formatted as described in section 3.1.2.4.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-2.  Alternatively the table 
 *            entries may be defined to suit a special purpose.  References: 
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *
 * Output Arguments:
 *   
 *   pSrcDstIsXr - pointer to the vector of requantized spectral samples for 
 *            the synthesis filter bank, in Q5.26 format (Qm.n defined in 
 *             Introduction ).  Only the first (pNonZeroBound[ch]+17)/18 
 *            18 point blocks are updated by the function.  This pointer must 
 *            be aligned on an 8-byte boundary. 
 *   pNonZeroBound - (Inputinput/output argumentparameter) pointer to the 
 *            spectral bound above which all coefficients are set to zero; for 
 *            stereo and dual-channel modes, the left channel bound is 
 *            pNonZeroBound[0], and the right channel bound is 
 *            pNonZeroBound[1]. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no errors 
 *    OMX_Sts_BadArgErr - bad arguments detected; either pSrcDstIsXr or 
 *              pBuffer are not 8-byte aligned, or one or more of the 
 *              following pointers is NULL: 
 *            - pSrcDstIsXr 
 *            - pNonZeroBound
 *            - pScaleFactor
 *            - pSideInfo
 *            - pFrameHeader
 *            - pBuffer, 
 *        Or one or more of the following input error conditions is 
 *              detected:
 *            - pNonZeroBound[ch] outside the range [0,576] 
 *            - pFrameHeader->id outside the range [0,1] 
 *            - pFrameHeader->samplingFreq outside the range [0,2] 
 *            - pFrameHeader->mode outside the range [0,3] 
 *            - pSideInfo[ch].blockType outside the range [0,3] 
 *            - pFrameHeader->modeExt outside the range [0,3] 
 *            - pSideInfo[ch].mixedBlock outside the range [0,1] 
 *            - pSideInfo[ch].globGain outside the range [0,255] 
 *            - pSideInfo[ch].sfScale outside the range [0,1] 
 *            - pSideInfo [ch].preFlag outside the range [0,1] 
 *            - pSideInfo [ch].pSubBlkGain[w] outside the range [0,7] 
 *            - |pSrcDstIsXr[i]| > 8206 
 *            - pScaleFactor[sfb] > 7 when pScaleFactor[sfb] is the 
 *              intensity position for an MPEG-1 stream 
 *            - pSideInfo[0].blockType != pSideInfo[1].blockType and 
 *              the bit stream is encoded in joint stereo mode 
 *            - pSideInfo[0].mixedBlock != pSideInfo[1].mixedBlock 
 *              and the bit stream is encoded in joint stereo mode.
 *
 *        Note: In the above list of error conditions the range of 
 *              the parameter <ch> is from 0 to chNum-1, and the range on the 
 *              parameter <w> is from 0 to 2, where chNum is the number of 
 *              channels associated with value of the pFrameHeader ->mode. 
 *              If pFrameHeader->mode == 3 then chNum==1, otherwise chNum==2. 
 *
 */
OMXResult omxACMP3_ReQuantize_S32_I (
    OMX_S32 *pSrcDstIsXr,
    OMX_INT *pNonZeroBound,
    OMX_S8 *pScaleFactor,
    OMXMP3SideInfo *pSideInfo,
    OMXMP3FrameHeader *pFrameHeader,
    OMX_S32 *pBuffer
);



/**
 * Function:  omxACMP3_ReQuantizeSfb_S32_I   (3.1.3.3.2)
 *
 * Description:
 * Requantizes the decoded Huffman symbols.  Spectral samples for the 
 * synthesis filter bank are derived from the decoded symbols using the 
 * requantization equations given in the ISO standard.  Stereophonic mid/side 
 * (M/S) and/or intensity decoding is applied if necessary.  Requantized 
 * spectral samples are returned in the vector pSrcDstIsXr. The reordering 
 * operation is applied for short blocks.  Users must preallocate a workspace 
 * buffer pointed to by pBuffer prior to calling the requantization function. 
 * The value pointed by pNonZeroBound will be recalculated according to the 
 * output data sequence.  Special scalefactor values on the right channel of 
 * intensity-coded scalefactor bands are used to indicate illegal intensity 
 * positions. References 1. [ISO11172-3], Table B.8 ( MPEG-1 ) 2. 
 * [ISO13818-3], sub-clause 2.5.3.2.2 and Table B.2 ( MPEG-2 ) 3. 
 * [ISO14496-3], sub-part 9,  MPEG-1/2 Audio in MPEG-4  
 *
 * Input Arguments:
 *   
 *   pSrcDstIsXr - pointer to the vector of decoded Huffman symbols; for 
 *            stereo and dual-channel modes, right channel data begins at the 
 *            address &(pSrcDstIsXr[576]). This pointer must be aligned on an 
 *            8-byte boundary. 
 *   pNonZeroBound - (Inout/output argument) pointer to the spectral bound 
 *            above which all coefficients are set to zero; for stereo and 
 *            dual-channel modes, the left channel bound is pNonZeroBound [0], 
 *            and the right channel bound is pNonZeroBound [1] 
 *   pScaleFactor - pointer to the scalefactor buffer; for stereo and 
 *            dual-channel modes, the right channel scalefactors begin at 
 *            &(pScaleFactor[OMX_MP3_SF_BUF_LEN]). Special scalefactor values 
 *            on the right channel of intensity-coded scalefactor bands shall 
 *            be used to indicate an illegal intensity position as follows: i) 
 *            for MPEG-1 streams (pFrameHeader->id==1), the value 7 
 *            [ISO11172-3, section 2.4.3.4.9.3], and ii) for MPEG-2 streams 
 *            (pFrameHeader->id==0), a negative value. 
 *   pSideInfo - pointer to the side information for the current granule 
 *   pFrameHeader - pointer to the frame header for the current frame 
 *   pBuffer - pointer to a workspace buffer. The buffer length must be 576 
 *            samples, and the pointer must be 8-byte aligned. 
 *   pSfbTableLong - pointer to the long block scalefactor band table, 
 *            formatted as described in section 3.1.2.3.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-1. Alternatively the table 
 *            entries may be defined to suit a special purpose. References:  
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *   pSfbTableShort - pointer to the short block scalefactor band table, 
 *            formatted as described in section 3.1.2.4.  Table entries 
 *            optionally may follow the MPEG-1, MPEG-2, or MPEG-4 audio 
 *            standards as shown in Example 3-2.  Alternatively the table 
 *            entries may be defined to suit a special purpose.  References: 
 *            [ISO11172-3], Table B.8, [ISO13818-3], Table B.2, [ISO14496-3], 
 *            sub-part 9. 
 *
 * Output Arguments:
 *   
 *   pSrcDstIsXr - pointer to the vector of requantized spectral samples for 
 *            the synthesis filter bank, in Q5.26 format (Qm.n defined in 
 *             Introduction ).  Only the first (pNonZeroBound[ch]+17)/18 
 *            18 point blocks are updated by the function.  This pointer must 
 *            be aligned on an 8-byte boundary. 
 *   pNonZeroBound - (Inputinput/output argumentparameter) pointer to the 
 *            spectral bound above which all coefficients are set to zero; for 
 *            stereo and dual-channel modes, the left channel bound is 
 *            pNonZeroBound[0], and the right channel bound is 
 *            pNonZeroBound[1]. 
 *
* Return Value:
 *    
 *    OMX_Sts_NoErr - no errors 
 *    OMX_Sts_BadArgErr - bad arguments detected; either pSrcDstIsXr or 
 *              pBuffer are not 8-byte aligned, or one or more of the 
 *              following pointers is NULL: 
 *            - pSrcDstIsXr 
 *            - pNonZeroBound
 *            - pScaleFactor
 *            - pSideInfo
 *            - pFrameHeader
 *            - pBuffer, 
 *        Or one or more of the following input error conditions is 
 *              detected:
 *            - pNonZeroBound[ch] outside the range [0,576] 
 *            - pFrameHeader->id outside the range [0,1] 
 *            - pFrameHeader->samplingFreq outside the range [0,2] 
 *            - pFrameHeader->mode outside the range [0,3] 
 *            - pSideInfo[ch].blockType outside the range [0,3] 
 *            - pFrameHeader->modeExt outside the range [0,3] 
 *            - pSideInfo[ch].mixedBlock outside the range [0,1] 
 *            - pSideInfo[ch].globGain outside the range [0,255] 
 *            - pSideInfo[ch].sfScale outside the range [0,1] 
 *            - pSideInfo [ch].preFlag outside the range [0,1] 
 *            - pSideInfo [ch].pSubBlkGain[w] outside the range [0,7] 
 *            - |pSrcDstIsXr[i]| > 8206 
 *            - pScaleFactor[sfb] > 7 when pScaleFactor[sfb] is the 
 *              intensity position for an MPEG-1 stream 
 *            - pSideInfo[0].blockType != pSideInfo[1].blockType and 
 *              the bit stream is encoded in joint stereo mode 
 *            - pSideInfo[0].mixedBlock != pSideInfo[1].mixedBlock 
 *              and the bit stream is encoded in joint stereo mode.
 *
 *        Note: In the above list of error conditions the range of 
 *              the parameter <ch> is from 0 to chNum-1, and the range on the 
 *              parameter <w> is from 0 to 2, where chNum is the number of 
 *              channels associated with value of the pFrameHeader ->mode. 
 *              If pFrameHeader->mode == 3 then chNum==1, otherwise chNum==2. 
 *
 */
OMXResult omxACMP3_ReQuantizeSfb_S32_I (
    OMX_S32 *pSrcDstIsXr,
    OMX_INT *pNonZeroBound,
    OMX_S8 *pScaleFactor,
    OMXMP3SideInfo *pSideInfo,
    OMXMP3FrameHeader *pFrameHeader,
    OMX_S32 *pBuffer,
    OMXMP3ScaleFactorBandTableLong pSfbTableLong,
    OMXMP3ScaleFactorBandTableShort pSfbTableShort
);



/**
 * Function:  omxACMP3_MDCTInv_S32   (3.1.3.4.1)
 *
 * Description:
 * Stage 1 of the hybrid synthesis filter bank. This performs the following 
 * operations: a) Alias reduction b) Inverse MDCT according to block size 
 * specifiers and mixed block modes c) Overlap add of IMDCT outputs, and d) 
 * Frequency inversion prior to PQMF bank Because the IMDCT is a lapped 
 * transform, the user must preallocate a buffer referenced by 
 * pSrcDstOverlapAdd to maintain the IMDCT overlap-add state. The buffer must 
 * contain 576 elements. Prior to the first call to the synthesis filter bank, 
 * all elements of the overlap-add buffer should be set equal to zero. In 
 * between all subsequent calls, the contents of the overlap-add buffer should 
 * be preserved. Upon entry to omxACMP3_MDCTInv_S32, the overlap-add buffer 
 * should contain the IMDCT output generated by operating on the previous 
 * granule; upon exit from omxACMP3_MDCTInv_S32, the overlap-add buffer will 
 * contain the overlapped portion of the output generated by operating on the 
 * current granule. Upon return from the function, the IMDCT sub-band output 
 * samples are organized as follows: pDstY[j*32+subband], for j=0 to 17; 
 * sub-band=0 to 31.  Note: The pointers pSrcXr (input argument) and pDstY 
 * (output argument) must reference different buffers. Reference [ISO13818-3], 
 * sub-clause 2.5.3.3.2 
 *
 * Input Arguments:
 *   
 *   pSrcXr - pointer to the vector of requantized spectral samples for the 
 *            current channel and granule, represented in Q5.26 format.  Note: 
 *            The vector buffer is used as a workspace buffer when the input 
 *            data has been processed. So the data in the buffer is 
 *            meaningless when exiting the function 
 *   pSrcDstOverlapAdd - pointer to the overlap-add buffer; contains the 
 *            overlapped portion of the previous granule s IMDCT output 
 *   nonZeroBound - the bound above which all spectral coefficients are zero 
 *            for the current granule and channel 
 *   pPrevNumOfImdct - pointer to the number of IMDCTs computed for the 
 *            current channel of the previous granule 
 *   blockType - block type indicator 
 *   mixedBlock - mixed block indicator 
 *
 * Output Arguments:
 *   
 *   pDstY - pointer to the vector of IMDCT outputs in Q7.24 format, for 
 *            input to PQMF bank 
 *   pSrcDstOverlapAdd - pointer to the updated overlap-add buffer; contains 
 *            overlapped portion of the current granule s IMDCT output 
 *   pPrevNumOfImdct - pointer to the number of IMDCTs, for current granule, 
 *            current channel 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments detected; one or more of the pointers 
 *              pSrcXr, pDstY, pSrcDstOverlapAdd, and/or pPrevNumOfImdct is 
 *              NULL 
 *    OMX_Sts_Err - one or more of the following input data errors detected: 
 *              either blockType exceeds [0,3], mixedBlock exceeds [0,1], 
 *              nonZeroBound exceeds [0,576], or *pPrevNumOfImdctexceeds 
 *              [0,32] 
 *
 */
OMXResult omxACMP3_MDCTInv_S32 (
    OMX_S32 *pSrcXr,
    OMX_S32 *pDstY,
    OMX_S32 *pSrcDstOverlapAdd,
    OMX_INT nonZeroBound,
    OMX_INT *pPrevNumOfImdct,
    OMX_INT blockType,
    OMX_INT mixedBlock
);



/**
 * Function:  omxACMP3_SynthPQMF_S32_S16   (3.1.3.4.2)
 *
 * Description:
 * Stage 2 of the hybrid synthesis filter bank; a critically-sampled 
 * 32-channel PQMF synthesis bank that generates 32 time-domain output samples 
 * for each 32-sample input block of IMDCT outputs. For each input block, the 
 * PQMF generates an output sequence of 16-bit signed little-endian PCM 
 * samples in the vector pointed to by pDstAudioOut. If mode equals 2, the 
 * left and right channel output samples are interleaved (i.e., LRLRLR), such 
 * that the left channel data is organized as follows: pDstAudioOut [2*i], i=0 
 * to 31. If mode equals 1, then the left and right channel outputs are not 
 * interleaved. A workspace buffer of size 512 x Number of Channels must be 
 * preallocated (pVBuffer). This buffer is referenced by the pointer pVBuffer, 
 * and its elements should be initialized to zero prior to the first call. 
 * During subsequent calls, the pVBuffer input for the current call should 
 * contain the pVbuffer output generated by the previous call. The state 
 * variable pVPosition should be initialized to zero and preserved between 
 * calls. The values contained in pVBuffer or pVPosition should be modified 
 * only during decoder reset, and the reset values should always be zero. 
 * Reference [ISO13818-3], sub-clause 2.5.3.3.2 
 *
 * Input Arguments:
 *   
 *   pSrcY - pointer to the block of 32 IMDCT sub-band input samples, in 
 *            Q7.24 format 
 *   pVBuffer - pointer to the input workspace buffer containing Q7.24 data. 
 *            The elements of this buffer should be initialized to zero during 
 *            decoder reset. During decoder operation, the values contained in 
 *            this buffer should be modified only by the PQMF function. 
 *   pVPosition - pointer to the internal workspace index; should be 
 *            initialized to zero during decoder reset. During decoder 
 *            operation, the value of this index should be preserved between 
 *            PQMF calls and should be modified only by the function. 
 *   mode - flag that indicates whether or not the PCM audio output channels 
 *            should be interleaved 1 - not interleaved 2 - interleaved 
 *
 * Output Arguments:
 *   
 *   pDstAudioOut - pointer to a block of 32 reconstructed PCM output samples 
 *            in 16-bit signed Q0.15 format (little-endian); left and right 
 *            channels are interleaved according to the mode flag. This should 
 *            be aligned on a 4-byte boundary 
 *   pVBuffer - pointer to the updated internal workspace buffer containing 
 *            Q7.24 data; see usage notes under input argument discussion 
 *   pVPosition - pointer to the updated internal workspace index; see usage 
 *            notes under input argument discussion 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - No errors 
 *    OMX_Sts_BadArgErr - bad arguments detected; one or more of the 
 *              following conditions is true: mode<1 or mode>2 
 *    -   *pVPosition is outside the range [0, 15] at least one of the 
 *              following pointers is NULL: pSrcY, pDstAudioOut, pVBuffer, 
 *              and/or pVPosition. 
 *
 */
OMXResult omxACMP3_SynthPQMF_S32_S16 (
    OMX_S32 *pSrcY,
    OMX_S16 *pDstAudioOut,
    OMX_S32 *pVBuffer,
    OMX_INT *pVPosition,
    OMX_INT mode
);



/* 3.2.2.1 ADIF Header  */

typedef struct {
    OMX_U32 ADIFId;        /** 32-bit, "ADIF" ASCII code */
    OMX_INT copyIdPres;    /** copy id flag: 0: off, 1: on */
    OMX_INT originalCopy;  /** 0: copy, 1: original */
    OMX_INT home;
    OMX_INT bitstreamType; /** 0: constant rate, 1: variable rate */
    OMX_INT bitRate;       /** bit rate. if 0, unkown bit rate */
    OMX_INT numPrgCfgElt;  /** number of program config elements */
    OMX_INT pADIFBufFullness[OMX_AAC_ELT_NUM]; /** buffer fullness */
    OMX_U8 pCopyId[9];     /** 72-bit copy id */
} OMXAACADIFHeader;



/* 3.2.2.2 ADTS Frame Header  */

typedef struct {
    /** ADTS fixed header */ 
    OMX_INT id;                /** ID 1*/
    OMX_INT layer;             /** layer index 
                                 *    0x3: Layer I 
                                 *    0x2: Layer II 
                                 *    0x1: Layer III 
                                 */
    OMX_INT protectionBit;     /** 0: CRC on, 1: CRC off */
    OMX_INT profile;           /** profile: 0:MP, 1:LP, 2:SSR */
    OMX_INT samplingRateIndex; /** sampling rate index */
    OMX_INT privateBit;        /** private_bit */
    OMX_INT chConfig;          /** channel configuration */
    OMX_INT originalCopy;      /** 0: copy, 1: original */
    OMX_INT home; 

    /** ADTS variable header */
    OMX_INT cpRightIdBit;      /** copyright id bit */
    OMX_INT cpRightIdStart;    /** copyright id start */
    OMX_INT frameLen;          /** frame length in bytes */
    OMX_INT ADTSBufFullness;   /** buffer fullness */
    OMX_INT numRawBlock;       /** number of raw data blocks in frame */
    
    /** ADTS CRC error check, 16 bits */ 
    OMX_INT CRCWord;           /** CRC-check word */
} OMXAACADTSFrameHeader;



/* 3.2.2.3 Individual Channel Stream Side Information  */

typedef struct {
    /** unpacked from the bitstream */ OMX_INT icsReservedBit;
    OMX_INT winSequence;        /** window sequence flag */
    OMX_INT winShape;           /** window shape, 0: sine, 1: KBD */
    OMX_INT maxSfb;             /** maximum effective scalefactor bands */
    OMX_INT sfGrouping;         /** scalefactor grouping flag */
    OMX_INT predDataPres;       /** 0: prediction off. 1: prediction on */
    OMX_INT predReset;          /** 0: reset off, 1: reset on */
    OMX_INT predResetGroupNum;  /** prediction reset group number */
    OMX_U8 pPredUsed[OMX_AAC_PRED_SFB_MAX+3];  /** prediction flag buffer for 
                                  * each scalefactor band: 0: off, 1: on; 
                                  * buffer length 44 bytes, 4-byte align 
                                  */

    /** decoded from the above info */ 
    OMX_INT numWinGrp;          /** number of window_groups */
    OMX_INT pWinGrpLen[OMX_AAC_GROUP_NUM_MAX]; /** buffer for windows in each group*/
} OMXAACIcsInfo;



/* 3.2.2.4 Program Configuration Element   */
/* The program configuration element (PCE) structure is defined below.  The elements of this structure correspond to the PCE syntactical unit defined in [ISO14496-3], subpart 4, section 4.5.1.2.  */

typedef struct {
    OMX_INT eltInsTag;            /* element instance tag */
    OMX_INT profile;              /* 0: main,  1: LC,  2: SSR  3: LTP */
    OMX_INT samplingRateIndex;    /* sampling rate index 
                                   * Reference: [ISO14496-3], Table 1.6.2 
                                   *  0: 96000,   1: 88200,   2: 64000,  3: 48000 
                                   *  4: 44100,   5: 32000,   6: 24000,  7: 22050 
                                   *  8: 16000,   9: 12000,  10: 11025, 11: 8000,
                                   * 12:  7350, 13/14: rsvd, 15: escape val 
                                   */
    
    OMX_INT numFrontElt;          /* number of front elements */
    OMX_INT numSideElt;           /* number of side elements */
    OMX_INT numBackElt;           /* number of back elements */
    OMX_INT numLfeElt;            /* number of LFE elements */
    OMX_INT numDataElt;           /* number of data elements */
    OMX_INT numValidCcElt;        /* number of channel coupling elements */
    OMX_INT monoMixdownPres;      /* mono mixdown flag: 0: off, 1: on */
    OMX_INT monoMixdownEltNum;    /* number of SCE that is the mixdown */ 
    OMX_INT stereoMixdownPres;    /* stereo mixdown flag: 0: off, 1: on */
    OMX_INT stereoMixdownEltNum;  /* number of CPE that is the mixdown */
    OMX_INT matrixMixdownIdxPres; /* matrix mixdown: 0: off, 1: on */
    OMX_INT matrixMixdownIdx;     /* matrix mixdown coef index */
    OMX_INT pseudoSurroundEnable; /* pseudo surround: 0: off, 1: on */
                
    OMX_INT pFrontIsCpe[OMX_AAC_ELT_NUM];  /* indicates whether the associated 
                                            * SCE or CPE is addressed as a front 
                                            * element. ‘0’ selects an SCE, 
                                            * ‘1’ selects a CPE. The instance 
                                            * of the SCE or CPE addressed is 
                                            * given by the corresponding entry in 
                                            * the pFrontElementTagSel array 
                                            */
    OMX_INT pFrontTagSel[OMX_AAC_ELT_NUM];     /* instance tags of the  SCE/CPE addressed as a front element */
    OMX_INT pSideIsCpe[OMX_AAC_ELT_NUM];       /* same as pFrontIsCPE, but for side elements */
    OMX_INT pSideTagSel[OMX_AAC_ELT_NUM];      /* same as pFrontTagSel, but for Side elements */
    OMX_INT pBackIsCpe[OMX_AAC_ELT_NUM];       /* same as pFrontIsCPE, but for back elements */
    OMX_INT pBackTagSel[OMX_AAC_ELT_NUM];      /* same as pFrontTagSel, but For back elements. */
    OMX_INT pLfeTagSel[OMX_AAC_LFE_ELT_NUM];   /* instance tag of the LFE addressed */
    OMX_INT pDataTagSel[OMX_AAC_DATA_ELT_NUM]; /* instance tag of the DSE addressed */
    OMX_INT pCceIsIndSw[OMX_AAC_ELT_NUM];      /* CCE independence bit flag; 
                                                * 0: corresponding CCE is not independently switched 
                                                * 1: corresponding CCE is independently switched 
                                                */
    OMX_INT pCceTagSel[OMX_AAC_ELT_NUM];        /* instance tags of the CCE addressed */
    OMX_INT numComBytes;                        /* length, in bytes, of the comment field */
    OMX_S8 pComFieldData[OMX_AAC_COMMENTS_LEN]; /* comment data */
} OMXAACPrgCfgElt;



/* 3.2.2.5 LTP Information  */

typedef struct {
    OMX_INT ltpDataPresent; /** 0: LTP used, 1: LTP not used */
    OMX_INT ltpLag;         /** LTP lag; range 0 to 2047 */
    OMX_S16 ltpCoef;        /** LTP coefficient, represented using Q14, valid in the range 9352 to 22438 */
    OMX_INT pLtpLongUsed[OMX_AAC_MAX_LTP_SFB+1]; /** SFB LTP indicators */
} OMXAACLtpInfo, *OMXAACLtpInfoPtr;



/* 3.2.2.6 Channel Pair Element  */

typedef struct {
    OMX_INT commonWin;      /** common window flag, 0: off, 1: on */
    OMX_INT msMaskPres;     /** MS stereo mask present flag */
    OMX_U8 ppMsMask[OMX_AAC_GROUP_NUM_MAX][OMX_AAC_SF_MAX]; /** MS stereo flag buffer for each SFB */
} OMXAACChanPairElt;



/* 3.2.2.7 Channel Information  */

typedef struct {
    OMX_INT tag;               /* element_instance_tag (0-15)*/
    OMX_INT id;                /* syntactic element id 
                                * 0: SCE, 1: CPE, 2: CCE, 3: LFE,
                                * 4: DSE, 5: PCE, 6: FIL, 7: END 
                                */
    OMX_INT samplingRateIndex; /* sample rate index Reference: [ISO14496-3], Table 1.6.2 
                                *  0: 96000, 1: 88200,  2: 64000,  3: 48000 
                                *  4: 44100, 5: 32000,  6: 24000,  7: 22050 
                                *  8: 16000, 9: 12000, 10: 11025, 11: 8000 
                                * 12: 7350, 13/14: rsvd, 15: escape val 
                                */
    OMX_INT predSfbMax;    /* maximum prediction scalefactor bands */
    OMX_INT preWinShape;   /* previous block window shape */
    OMX_INT winLen;        /* 128: short window, 1024: others */
    OMX_INT numWin;        /* 1: long block, 8: short block */
    OMX_INT numSwb;        /* depends on sample freq. + block type */
    OMX_INT globGain;      /* global gain */
    OMX_INT pulseDataPres; /* pulse data present flag, 0: off, 1: on */
    OMX_INT tnsDataPres;   /* TNS data present flag, 0: off, 1: on */
    OMX_INT gainContrDataPres; /* gc data present flag, 0: off, 1: on */
    OMXAACIcsInfo *pIcsInfo;   /* pointer to OMXAACIcsInfo struct */
    OMXAACChanPairElt *pChanPairElt; /* ptr to OMXAACChanPairElt struct */
    OMX_U8 pSectCb[OMX_AAC_SF_LEN];  /* section codebook buffer */
    OMX_U8 pSectEnd[OMX_AAC_SF_LEN]; /* last SFB in each section */
    OMX_INT pMaxSect[OMX_AAC_GROUP_NUM_MAX];     /* num sections each group*/
    OMX_INT pTnsNumFilt[OMX_AAC_GROUP_NUM_MAX];  /* num TNS filters */
    OMX_INT pTnsFiltCoefRes[OMX_AAC_GROUP_NUM_MAX]; /* TNS coef res flags */
    OMX_INT pTnsRegionLen[OMX_AAC_TNS_FILT_MAX]; /* TNS filter lens */
    OMX_INT pTnsFiltOrder[OMX_AAC_TNS_FILT_MAX]; /* TNS filter orders */
    OMX_INT pTnsDirection[OMX_AAC_TNS_FILT_MAX]; /* TNS filter dirs */
} OMXAACChanInfo;



/**
 * Function:  omxACAAC_UnpackADIFHeader   (3.2.3.1.1)
 *
 * Description:
 * Unpacks the AAC ADIF format header and all program configuration elements 
 * from the input bit stream and copies the contents into the ADIF header and 
 * program configuration element data structures. 
 *
 * Reference: [ISO14496-3], Table 1.A.2 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the current byte before the ADIF header 
 *   prgCfgEltMax - the maximum allowed number of program configuration 
 *            elements; an error is returned by the function if the value of 
 *            the parameter numPrgCfgElt encountered in the input stream is 
 *            larger than prgCfgEltMax 
 *   pADIFHeader - pointer to an uninitialized OMXACCADIFHeader structure 
 *   pPrgCfgElt - pointer to an array of uninitialized OMXAACPrgCfgElt 
 *            structures.  There should be sufficient space in the buffer to 
 *            contain prgCfgEltMax elements. 
 *
 * Output Arguments:
 *   
 *   ppBitStream - double pointer to the current byte after the ADIF header 
 *   pADIFHeader - pointer to the updated OMXACCADIFHeader structure. 
 *            UnpackADIFHeader updates all OMXAACADIFHeader members to reflect 
 *            the contents of the ADIF header in the input stream, with the 
 *            following limitations:  i) the member pCopyId is updated only if 
 *            copyIdPres==1; ii) Elements 0, 1,  , numPrgCfgElt-1 of the 
 *            pADIFFullness array are updated only if bistreamType==0. The 
 *            other elements of ADIF fullness array are not modified. 
 *   pPrgCfgElt - pointer to the updated array of OMXAACPrgCfgElt structures. 
 *            All program configuration element structures (i.e., elements 0, 
 *            1,  , (pADIFHeader->numPrgCfgElt)-1 of the array pPrgCfgElt) are 
 *            updated by the function to reflect the contents of the ADIF 
 *            header in the input stream, with the following limitations for 
 *            each PCE structure:  i) the member monoMixdownEltNum is updated 
 *            only if monoMixdownPres==1;  ii) the member stereoMixdownEltNum 
 *            is updated only if stereoMixdownPres==1; iii) the members 
 *            matrixMixdownIdx and pseudoSourroundEnable are updated only if 
 *            matrixMixdownIdxPres==1; iv) Only elements 0, 1,  , 
 *            numFrontElt-1 of the pFrontIsCpe and pFrontTagSel arrays are 
 *            updated; v) Only elements 0, 1,  , numSideElt-1 of the 
 *            pSideIsCpe and pSideTagSel arrays are updated; vi) Only elements 
 *            0, 1,  , numBackElt-1 of the pBackIsCpe and pBackTagSel arrays 
 *            are updated; vii) Only elements 0, 1,  , numLfeElt-1 of the 
 *            pLfeTagSel array are updated; viii) Only elements 0, 1,  , 
 *            numDataElt-1 of the pDataTagSel array are updated; ix) Only 
 *            elements 0, 1,  , numValidCcElt-1 of the pCceIsIndSw and 
 *            pCceTagSel arrays are updated; x) Only elements 0, 1,  , 
 *            numComBytes-1 of the pComFieldData array are updated. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    At least one of the following pointers is NULL: 
 *           - ppBitStream, 
 *           - pADIFHeader, 
 *           - pPrgCfgElt 
 *           - *ppBitStream
 *    -    prgCfgEltMax exceeds [1, 16] 
 *    OMX_StsACAAC_PrgNumErr - the decoded pADIFHeader->numPrgCfgElt > 
 *              prgCfgEltMax.  
 *
 * Note: pADIFHeader->numPrgCfgElt is the number directly unpacked 
 * from bit stream plus 1. prgCfgEltMax is the number of the program 
 * configuration elements that the user wants to support. The valid 
 * range is [1, 16] 
 *
 */
OMXResult omxACAAC_UnpackADIFHeader (
    const OMX_U8 **ppBitStream,
    OMXAACADIFHeader *pADIFHeader,
    OMXAACPrgCfgElt *pPrgCfgElt,
    OMX_INT prgCfgEltMax
);



/**
 * Function:  omxACAAC_UnpackADTSFrameHeader   (3.2.3.1.2)
 *
 * Description:
 * Unpacks the ADTS frame header from the input bit stream and copies the 
 * contents into an ADTS header data structure.  If the ADTS protection bit is 
 * asserted (pADTSFrameHeader->protectionBit==0) then the 16-bit CRC word is 
 * copied into pADTSFrameHeader->CRCWord. The first byte is stored in 
 * pADTSFrameHeader->CRCWord[15:8], and the second byte is stored in 
 * pADTSFrameHeader >CRCWord[7:0]. This function does not test for header 
 * corruption. 
 *
 * Reference: [ISO14496-3], Table 1.A.6 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the current byte in the input stream 
 *   pADTSFrameHeader - pointer an uninitialized OMXACCADTSHeader structure 
 *
 * Output Arguments:
 *   
 *   ppBitStream - double pointer to the current byte after unpacking the 
 *            ADTS frame header. 
 *   pADTSFrameHeader - pointer to the updated OMXAACADTSFrameHeader 
 *            structure.  All ADTS header structure members are updated by the 
 *            function to reflect the contents of the ADTS header in the input 
 *            stream.  The structure member CRCWord is updated only if 
 *            protectionBit==1. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments.  
 *    -    At least one of the following pointers is NULL : 
 *            -  ppBitStream, 
 *            - *ppBitStream, 
 *            -  pADTSFrameHeader
 *
 */
OMXResult omxACAAC_UnpackADTSFrameHeader (
    const OMX_U8 **ppBitStream,
    OMXAACADTSFrameHeader *pADTSFrameHeader
);



/**
 * Function:  omxACAAC_DecodePrgCfgElt   (3.2.3.1.3)
 *
 * Description:
 * Unpacks one program configuration element (PCE) from the input bit stream 
 * and copies the contents into a PCE data structure. 
 *
 * Reference [ISO14496-3], sub-clause 4.5.1.2 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the current byte 
 *   pOffset - pointer to the bit position in the byte pointed by 
 *            *ppBitStream..  Valid within the range 0 to 7; 0: MS bit, 7: LS 
 *            bit. 
 *   pPrgCfgElt - pointer an uninitialized OMXACCPrgCfgElt structure 
 *
 * Output Arguments:
 *   
 *   ppBitStream - updated double pointer to the current byte after decoding 
 *            the PCE. 
 *   pOffset - pointer to the bit position in the byte pointed by 
 *            *ppBitStream. Valid within the range 0 to 7. 0: MS bit, 7: LS 
 *            bit. 
 *   pPrgCfgElt - pointer to updated OMXAACPrgCfgElt structure.  All 
 *            structures members are updated by the function to reflect the 
 *            contents of the program configuration element in the input 
 *            stream, with the following limitations:  i) the member 
 *            monoMixdownEltNum is updated only if monoMixdownPres==1, 
 *            otherwise it is set equal to 0; ii) the member 
 *            stereoMixdownEltNum is updated only if stereoMixdownPres==1, 
 *            otherwise it is set equal to 0; iii) the members 
 *            matrixMixdownIdx and pseudoSourroundEnable are updated only if 
 *            matrixMixdownIdxPres==1; iv) Only elements 0, 1,  , 
 *            numFrontElt-1 of the pFrontIsCpe and pFrontTagSel arrays are 
 *            updated; v) Only elements 0, 1,  , numSideElt-1 of the 
 *            pSideIsCpe and pSideTagSel arrays are updated; vi) Only elements 
 *            0, 1,  , numBackElt-1 of the pBackIsCpe and pBackTagSel arrays 
 *            are updated; vii) Only elements 0, 1,  , numLfeElt-1 of the 
 *            pLfeTagSel array are updated; viii) Only elements 0, 1,  , 
 *            numDataElt-1 of the pDataTagSel array are updated; ix) Only 
 *            elements 0, 1,  , numValidCcElt-1 of the pCceIsIndSw and 
 *            pCceTagSel arrays are updated; x) Only elements 0, 1,  , 
 *            numComBytes-1 of the pComFieldData array are updated 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - Bad arguments 
 *    -    At least one of the following pointers is NULL: ppBitStream, pOffset, 
 *              pPrgCfgElt, *ppBitStream  
 *    -   *pOffset exceeds [0, 7] 
 *
 */
OMXResult omxACAAC_DecodePrgCfgElt (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMXAACPrgCfgElt *pPrgCfgElt
);



/**
 * Function:  omxACAAC_DecodeChanPairElt   (3.2.3.1.4)
 *
 * Description:
 * Decodes the contents of a channel pair element (CPE) from the input 
 * bitstream and copies the information into the output CPE data structure 
 * *pChanPairElt, output individual channel stream side information structure 
 * (ICS) *pIcsInfo, and output array of LTP information structures 
 * *(pLtpInfo[0]), *(pLtpInfo[1]). Updates are conditional and depend on the 
 * contents of the bitstream.  The ICS information structure *pIcsInfo is 
 * updated only if the flag parameter pChanPairElt->CommonWin== 1.  The array 
 * of LTP information structures *pLtpInfo is updated only if 
 * (pChanPairElt >CommonWin == 1) && (audioObjectType==4). If 
 * pChanPairElt->CommonWin == 0, then DecodeChanPairElt updates only the 
 * structure member pChanPairElt->commonWin, and all other *pIcsInfo, 
 * *pChanPairElt, and *pLtpInfo structure members/array elements remain 
 * unchanged. 
 *
 * Reference: [ISO14496-3], Table 4.5 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the current byte in the input bitstream 
 *   pOffset - pointer to the next available bit of the input bitstream byte 
 *            referenced by *ppBitStream. Valid in the range 0 to 7, where 0 
 *            signifies the most significant bit and 7 signifies the least 
 *            significant bit 
 *   audioObjectType - index of the audio object type:  2=LC, 4=LTP 
 *
 * Output Arguments:
 *   
 *   ppBitStream - double pointer to the current byte in the input bitstream, 
 *            updated after decoding the channel pair element. 
 *   pOffset - pointer to the next available bit of the input bitstream byte 
 *            referenced by *ppBitStream. Valid in the range 0 to 7, where 0 
 *            signifies the most significant bit and 7 signifies the least 
 *            significant bit. 
 *   pIcsInfo - if pChanPairElt->CommonWin == 1, then pIcsInfo points to the 
 *            updated OMXAACIcsInfo structure.  In this case, all structure 
 *            elements are updated unconditionally except as shown in Table 
 *            3-9.  Also in this case, only the first pIcsInfo->numWinGrp 
 *            elements in pIcsInfo-> pWinGrpLen are updated. Otherwise, if 
 *            pChanPairElt->CommonWin == 0 then none of the structure members 
 *            are modified; in this case the array pIcsInfo will be updated by 
 *            the function omxACAAC_NoiselessDecode. 
 *   pChanPairElt - pointer to the updated OMXAACChanPairElt structure.  The 
 *            function modifies the commonWin structure member 
 *            unconditionally, but modifies the other members (msMaskPres and 
 *            ppMsMask array) only if (pChanPairElt->CommonWin == 1, as shown 
 *            in Table 3-10. 
 *   pLtpInfo - array containing two LTP information structure pointers.  If 
 *            (pChanPairElt >CommonWin == 1) && (audioObjectType==4), then the 
 *            structures referenced by the pointers in this array are updated 
 *            to contain the LTP information associated with the individual 
 *            channels in the current CPE on which LTP has been enabled.  Four 
 *            update scenarios are possible:  i) no LTP information - if the 
 *            bit field predictor_data_present==0 within the ics_info syntax 
 *            element of the current CPE, then the array pLtpInfo is not 
 *            modified ii) LTP on the first CPE channel - if the elementary 
 *            stream bit field predictor_data_present==1 && the first 
 *            occurrence of the bit field ltp_data_present==1 then the 
 *            contents of *(pLtpInfo[0]) are updated; iii) LTP on the second 
 *            CPE channel - if predictor_data_present==1 && the second 
 *            occurrence of the bit field ltp_data_present==1 then the 
 *            contents of *(pLtpInfo[1]) are updated; iv) LTP on both CPE 
 *            channels - both cases ii) and iii) may occur simultaneously, in 
 *            which case both array elements are updated.  Otherwise, if 
 *            (pChanPairElt->CommonWin == 0) || (audioObjectType!=4) then the 
 *            function omxACAAC_DecodeChanPairElt does not update the contents 
 *            of the structures referenced by the pointers in the array.  
 *            Under this condition, the array will be updated later in the 
 *            stream decoding process by the function 
 *            omxACAAC_NoiselessDecode. Reference:  [ISO14496-3], sub-clause 
 *            4.4.2.1 and Tables 4.5-4.6. Table 3-9: pIcsInfo Members Modified 
 *            Conditionally by DecodeChanPairElt Members  Required Condition 
 *            SfGrouping pChanPairElt->CommonWin == 1 && pIcsInfo >winSequence 
 *            == 2 Members  Required Condition predResetGroupNum  Never 
 *            modified predReset  Never modified pPredUsed[.]  Never modified 
 *            Table 3-10: pChanPairElt Members Modified Conditionally by 
 *            DecodeChanPairElt Members Required Condition msMaskPres 
 *             pChanPairElt->CommonWin == 1 pMsMask[sfb] 
 *            pChanPairElg->CommonWin==1 && pChanPairElt >msMaskPres == 1 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    at least one of the following pointers is NULL: ppBitStream, 
 *              pOffset, *ppBitStream, pIcsInfo or pChanPairElt. 
 *    -   *pOffset exceeds [0, 7] 
 *    -    audioObjectType!=2 && audioObjectType!=4 
 *    OMX_Sts_InvalidBitstreamValErr - invalid bitstream parameter value 
 *              detected 
 *    -    commonWin==1 and the decoded value of pIcsInfo->maxSfb is out of 
 *              range, i.e., exceeds [0,51] 
 *
 */
OMXResult omxACAAC_DecodeChanPairElt (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMXAACIcsInfo *pIcsInfo,
    OMXAACChanPairElt *pChanPairElt,
    OMX_INT audioObjectType,
    OMXAACLtpInfoPtr *pLtpInfo
);



/**
 * Function:  omxACAAC_DecodeDatStrElt   (3.2.3.1.5)
 *
 * Description:
 * Gets data_stream_element from the input bit stream. 
 *
 * Reference: [ISO14496-3], Table 4.10 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to the current byte 
 *   pOffset - pointer to the bit position in the byte pointed by 
 *            *ppBitStream. Valid within 0 to 7. 0: MSB of the byte, 7: LSB of 
 *            the byte. 
 *
 * Output Arguments:
 *   
 *   ppBitStream - double pointer to the current byte after the decode data 
 *            stream element 
 *   pOffset - pointer to the bit position in the byte pointed by 
 *            *ppBitStream. Valid within 0 to 7. 0: MSB of the byte, 7: LSB of 
 *            the byte. 
 *   pDataTag - pointer to element_instance_tag. 
 *   pDataCnt - pointer to the value of length of total data in bytes 
 *   pDstDataElt - pointer to the data stream buffer that contains the data 
 *            stream extracted from the input bit stream.  There are 512 
 *            elements in the buffer pointed by pDstDataElt. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    At least one of the following pointers is NULL: 
 *           - ppBitStream,
 *           - pOffset, 
 *           - *ppBitStream, 
 *           - pDataTag, 
 *           - pDataCntor 
 *           - pDstDataElt. 
 *    -  *pOffset exceeds [0, 7] 
 *
 */
OMXResult omxACAAC_DecodeDatStrElt (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMX_INT *pDataTag,
    OMX_INT *pDataCnt,
    OMX_U8 *pDstDataElt
);



/**
 * Function:  omxACAAC_DecodeFillElt   (3.2.3.1.6)
 *
 * Description:
 * Gets the fill element from the input bit stream. 
 *
 * Reference: [ISO14496-3], Table 4.11 
 *
 * Input Arguments:
 *   
 *   ppBitStream - pointer to the pointer to the current byte 
 *   pOffset - pointer to the bit position in the byte pointed by 
 *            *ppBitStream. Valid within 0 to 7. 0: MSB of the byte, 7: LSB of 
 *            the byte 
 *
 * Output Arguments:
 *   
 *   ppBitStream - pointer to the pointer to the current byte after the 
 *            decode fill element 
 *   pOffset - pointer to the bit position in the byte pointed by 
 *            *ppBitStream. Valid within 0 to 7. 0: MSB of the byte, 7: LSB of 
 *            the byte. 
 *   pFillCnt - pointer to the value of the length of total fill data in 
 *            bytes 
 *   pDstFillElt- pointer to the fill data buffer of length 270 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    At least one of the following pointers is NULL:  
 *           - ppBitStream, 
 *           - pOffset, 
 *           - *ppBitStream, 
 *           - pFillCnt,
 *           - pDstFillElt.
 *    -   *pOffset exceeds [0, 7] 
 *
 */
OMXResult omxACAAC_DecodeFillElt (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMX_INT *pFillCnt,
    OMX_U8 *pDstFillElt
);



/**
 * Function:  omxACAAC_QuantInv_S32_I   (3.2.3.2.1)
 *
 * Description:
 * Inverse quantize the Huffman symbols for current channel. The equation is 
 * shown below:
 *
 *  pSrcDst[i] = sign( pSrcDst[i]) * (pSrcDst[i])^(4/3) * 2^( pScalefactor[sfb] - 100))
 *
 * Reference: [ISO14496-3], sub-clause 4.6.1 
 *
 * Input Arguments:
 *   
 *   pSrcDstSpectralCoef - pointer to the quantized coefficients extracted 
 *            from the input stream by the Huffman decoder.  The quantized 
 *            coefficients are integer values represented using Q0, i.e., no 
 *            scaling.  For short blocks the coefficients are interleaved by 
 *            scalefactor window bands in each group. Buffer must have 
 *            sufficient space to contain 1024 elements. 
 *   pScalefactor - pointer to the scalefactor buffer, of length 120 
 *   numWinGrp - group number 
 *   pWinGrpLen - pointer to the number of windows in each group, of length 8 
 *   maxSfb - max scalefactor bands number for the current block 
 *   pSfbCb - pointer to the scalefactor band codebook, of length 120. Only 
 *            maxSfb elements for each group are meaningful. There are no 
 *            spaces between the sequence groups. 
 *   samplingRateIndex - sampling rate index. Valid in [0, 11] 
 *   winLen - the data number in one window 
 *
 * Output Arguments:
 *   
 *   pSrcDstSpectralCoef - pointer to the inverse quantized coefficient 
 *            array, in Q28.3 format and of length 1024. For short blocks, the 
 *            coefficients are interleaved by scalefactor window bands in each 
 *            group. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments:
 *    -    At least one of the following pointers is NULL: 
 *         - pSrcDstSpectralCoef, 
 *         - pScalefactor, 
 *         - pWinGrpLenor 
 *         - pSfbCb 
 *    -    If short block, and numWinGrp exceeds [1, 8] 
 *    -    If long block, and numWinGrp!= 1 
 *    -    maxSfb exceeds [0, 51] 
 *    -    samplingRateIndex exceeds [0, 11] 
 *    -    winLen is neither 1024 nor 128 
 *    OMX_StsACAAC_CoefValErr - an input coefficient value contained in the 
 *              array referenced by pSrcDstSpectralCoef exceeds the 
 *              range [-8191, 8191].
 *    OMX_StsACAAC_MaxSfbErr - invalid maxSfb value in relation to numSwb 
 *
 */
OMXResult omxACAAC_QuantInv_S32_I (
    OMX_S32 *pSrcDstSpectralCoef,
    const OMX_S16 *pScalefactor,
    OMX_INT numWinGrp,
    const OMX_INT *pWinGrpLen,
    OMX_INT maxSfb,
    const OMX_U8 *pSfbCb,
    OMX_INT samplingRateIndex,
    OMX_INT winLen
);



/**
 * Function:  omxACAAC_DecodeMsStereo_S32_I   (3.2.3.3.1)
 *
 * Description:
 * Performs M-S stereo decoding; converts the MS stereo jointly-coded 
 * scalefactor bands of a channel pair from the M-S representation to the L-R 
 * representation; also performs the invert_intensity(group,sfb) function and 
 * stores the values in the pSfbCb buffer.  If invert_intensity(group, sfb) = 
 *  1, and if *pSfbCb = INTERITY_HCB, let *pSfbCb = INTERITY_HCB2; else if 
 * *pSfbCb = INTERITY_HCB2, let *pSfbCb = INTERITY_HCB. For scalefactor bands 
 * in which the MS stereo flag is asserted, the individual left and right 
 * channel spectral samples pSrcDstL[i] and pSrcDstR[i] are computed as 
 * follows: 
 *            pSrcDstL'[i] = pSrcDstL[i] + pSrcDstR[i], 
 *            pSrcDstR'[i] = pSrcDstL[i] - pSrcDstR[i]. 
 *
 * Reference: [ISO14496-3], sub-clause 4.6.8.1 
 *
 * Input Arguments:
 *   
 *   pSrcDstL - pointer to left channel data in Q28.3 format. For short 
 *            blocks, the coefficients are interleaved by scalefactor window 
 *            bands in each group, of length 1024. pSrcDstL must be 8-byte 
 *            aligned. 
 *   pSrcDstR - pointer to right channel data in Q28.3 format. For short 
 *            block, the coefficients are interleaved by scalefactor window 
 *            bands in each group, of length 1024. pSrcDstR must be 8-byte 
 *            aligned. 
 *   pChanPairElt - pointer to a Channel Pair Element structure that has been 
 *            previously populated. At minimum, the contents of msMaskPres and 
 *            pMsUsed fields are used to control MS decoding process and must 
 *            be valid.  These provide, respectively, the MS stereo mask for a 
 *            scalefactor band (0: MS Off, 1: MS On, 2: all bands on), and the 
 *            MS stereo flag buffer, of length 120. 
 *   pSfbCb -pointer to the scalefactor band codebook, of length 120.  Stores 
 *            maxSfb elements for each group.  There is no space between the 
 *            sequence groups 
 *   numWinGrp - group number 
 *   pWinGrpLen - pointer to the number of windows in each group, of length 8 
 *   maxSfb - max scalefactor bands number for the current block 
 *   samplingRateIndex - sampling rate index; valid in the range [0, 11] 
 *   winLen - the data number in one window 
 *
 * Output Arguments:
 *   
 *   pSrcDstL - pointer to left channel data in Q28.3 format. For short 
 *            blocks, the coefficients are interleaved by scalefactor window 
 *            bands in each group, of length 1024. pSrcDstL must be 8-byte 
 *            aligned. 
 *   pSrcDstR - pointer to right channel data in Q28.3 format. For short 
 *            blocks, the coefficients are interleaved by scalefactor window 
 *            bands in each group, of length 1024. pSrcDstR must be 8-byte 
 *            aligned. 
 *   pSfbCb- pointer to the scalefactor band codebook. If invert_intensity 
 *            group, sfb) = -1, and if *pSfbCb = INTERITY_HCB, let *pSfbCb = 
 *            INTERITY_HCB2; else if *pSfbCb = INTERITY_HCB2, let *pSfbCb = 
 *            INTERITY_HCB. Buffer length is 120. Store maxSfb elements for 
 *            each group. There is no space between the sequence groups. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments:
 *    -       At least one of the following pointers is NULL:
 *            - pSrcDstL, 
 *            - pSrcDstR, 
 *            - pMsUsed, 
 *            - pWinGrpLen, 
 *            - pSfbCb 
 *    -     pSrcDstL or pSrcDstR is not 8-byte aligned 
 *    -     For short blocks, numWinGrpexceeds [1, 8] 
 *    -     For long blocks, numWinGrp != 1 
 *    -     maxSfb exceeds [0, 51] 
 *    -     msMaskPres exceeds [1, 2] 
 *    -     samplingRateIndex exceeds [0, 11] 
 *    -     winLen is neither 1024 nor 128 
 *    OMX_StsACAAC_MaxSfbErr - invalid maxSfb value in relation to numSwb 
 *
 */
OMXResult omxACAAC_DecodeMsStereo_S32_I (
    OMX_S32 *pSrcDstL,
    OMX_S32 *pSrcDstR,
    OMXAACChanPairElt *pChanPairElt,
    OMX_U8 *pSfbCb,
    OMX_INT numWinGrp,
    const OMX_INT *pWinGrpLen,
    OMX_INT maxSfb,
    OMX_INT samplingRateIndex,
    OMX_INT winLen
);



/**
 * Function:  omxACAAC_DecodeIsStereo_S32   (3.2.3.3.2)
 *
 * Description:
 * Decodes jointly-coded scalefactor bands into discrete L/R stereo pairs for 
 * scalefactor bands in which the intensity stereo indicator flag stored in 
 * pSfbCb[sfb] is asserted. As described in [ISO14496-3], the discrete L/R 
 * signals pSrcL[i], pDstR[i] are recovered from the intensity-coded 
 * representation (single channel spectral coefficients + scalefactor) using 
 * the scaling operation expressed below.  The parameter invert_intensity(g, 
 * sfb) is not used in the formula, since it decoded and stored in pSfbCb[sfb] 
 * by the MS stereo decoder. 
 *
 *    pDstR[i] = pSrcL[i]*is_intensity(g, sfb) * 2^(-0.25*pScalefactor[sfb])
 *
 * Reference: [ISO14496-3], sub-clause 4.6.8.2 
 *
 * Input Arguments:
 *   
 *   pSrcL - pointer to left channel data in Q28.3 format. For short block, 
 *            the coefficients are interleaved by scalefactor window bands in 
 *            each group. Buffer length is 1024. pSrcL must be 8-byte aligned. 
 *   pScalefactor - pointer to the scalefactor buffer, of length 120 
 *   pSfbCb - pointer to the scalefactor band codebook, of length 120. Store 
 *            maxSfb elements for each group. There are no spaces between the 
 *            sequence groups. 
 *   numWinGrp - group number 
 *   pWinGrpLen - pointer to the number of windows in each group, of length 8 
 *   maxSfb -Max scalefactor bands number for the current block 
 *   samplingRateIndex - sampling rate index. Valid in [0, 11] 
 *   winLen - the data number in one window 
 *
 * Output Arguments:
 *   
 *   pDstR - pointer to right channel data in Q28.3 format. For short block, 
 *            the coefficients are interleaved by scalefactor window bands in 
 *            each group. Buffer length is 1024. pDstR must be 8-byte aligned. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments At least one of the following 
 *              pointers: pSrcL, pDstR, pWinGrpLen, pScalefactor, pSfbCbis 
 *              NULL. 
 *    If pSrcL, pDstR is not 8-byte aligned. 
 *    If short block, numWinGrpexceeds [1, 8] If long block, numWinGrp!= 1 
 *              maxSfbexceeds [0, 51] samplingRateIndexexceeds [0, 11] 
 *              winLenis neither 1024 nor 128 
 *    OMX_StsACAAC_MaxSfbErr - invalid maxSfb value in relation to numSwb 
 *
 */
OMXResult omxACAAC_DecodeIsStereo_S32 (
    const OMX_S32 *pSrcL,
    OMX_S32 *pDstR,
    const OMX_S16 *pScalefactor,
    const OMX_U8 *pSfbCb,
    OMX_INT numWinGrp,
    const OMX_INT *pWinGrpLen,
    OMX_INT maxSfb,
    OMX_INT samplingRateIndex,
    OMX_INT winLen
);



/**
 * Function:  omxACAAC_DecodeTNS_S32_I   (3.2.3.4.1)
 *
 * Description:
 * This function applies all-pole Temporal Noise Shaping (TNS) decoding 
 * filters to selected spectral coefficient regions. The output sequence is 
 * ready for the IMDCT synthesis bank. 
 *
 * Reference [ISO14496-3], sub-clause 4.6.9 
 *
 * Input Arguments:
 *   
 *   pSrcDstSpectralCoefs - spectral coefficient input vector, of length 
 *            1024, represented using Q28.3 format 
 *   pTnsNumFilt - pointer to a table containing the number of TNS filters 
 *            that are applied on each window of the current frame. The table 
 *            elements are indexed as follows: pTnsNumFilt[w], w=0 to 
 *            numWin-1; depending upon the current window sequence, this 
 *            vector may contain up to 8 elements. 
 *   pTnsRegionLen - pointer to a table containing TNS region lengths (in 
 *            scalefactor band units) for all regions and windows on the 
 *            current frame; the table entry pTnsRegionLen[i] specifies the 
 *            region length for k-th filter on the w-th window. The table 
 *            index, i, is computed as follows: 
 *
 *                       i = SUM[j=0,w-1] pTnsNumFilt[ j] + k 
 *
 *              where 0 <= w <= numWin-1, and 0 <= k <= pTnsNumFilt[w]-1. 
 *
 *   pTnsFiltOrder - pointer to a table containing TNS filter orders for all 
 *            regions and windows on the current frame; the table entry 
 *            pTnsFiltOrder[i] specifies the TNS filter order for the k-th 
 *            filter on the w-th window. The table index, i, is computed as 
 *            follows:
 *
 *                       i = SUM[j=0,w-1] pTnsNumFilt[ j] + k 
 *
 *              where 0 <= w <= numWin-1, and 0 <= k <= pTnsNumFilt[w]-1. 
 *
 *   pTnsFiltCoefRes - pointer to a table of TNS filter coefficient 
 *            resolution indicators for each window on the current frame. 
 *            Resolutions for filters on the w-th window are specified in 
 *            table entry pTnsFiltCoefRes[w], and w=0 to numWin-1. 
 *   pTnsFiltCoef - pointer to a table containing the complete set of TNS 
 *            filter coefficients for all windows and regions on the current 
 *            frame. Filter coefficients are stored contiguously in 
 *            filter-major order, i.e., the table is organized such that the 
 *            filter coefficients for the k-th filter of the w-th window are 
 *            indexed using pTnsFiltCoef[w][k][i], where 0 <= i <= 
 *            pTnsFiltOrder[j]-1, 0 <= k <= pTnsNumFilt[w]-1, 0 <= w <= 
 *            numWin-1, and the filter order index j is computed as shown 
 *            above. 
 *   pTnsDirection - pointer to a table of tokens that indicate the direction 
 *            of TNS filtering for all regions and windows on the current 
 *            frame, with 0 indicating upward and 1 indicating downward; in 
 *            particular the table entry pTnsDirection[i] specifies direction 
 *            for k-th filter on the w-th window, and the table index, i, is 
 *            computed as follows:
 * 
 *                       i = SUM[j=0,w-1] pTnsNumFilt[ j] + k 
 *
 *               where 0 <= w <= numWin-1, and 0 <= k <= pTnsNumFilt[w]-1. 
 *
 *   maxSfb - number of scalefactor bands transmitted per window group on the 
 *            current frame 
 *   profile -profile index, 0=main, 1=LC, 2=SSR as shown in [ISO14496-3], 
 *            Table 4.6.9 
 *   samplingRateIndex - sample rate index for the current frame 
 *   winLen - window length 
 *
 * Output Arguments:
 *   pSrcDstSpectralCoefs - pointer to the output spectral coefficients after 
 *            TNS filtering represented using Q28.3 format. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    At least one of the following pointers is NULL: 
 *            - pSrcDstSpectralCoefs, 
 *            - pTnsNumFilt, 
 *            - pTnsRegionLen, 
 *            - pTnsFiltOrder, 
 *            - pTnsFiltCoefRes, 
 *            - pTnsFiltCoef,
 *            - pTnsDirection. 
 *    -    profile!=1 
 *    -    samplingRateIndex exceeds [0, 11] 
 *    -    winLen!=128 and winLen!=1024 
 *    -    maxSfb exceeds [0,51] 
 *    OMX_StsACAAC_TnsNumFiltErr - for a short window sequence, 
 *              pTnsNumFilt[w] exceeds [0, 1]; For long window sequence, 
 *              pTnsNumFilt[w] exceeds [0, 3], w=0 to numWin-1. 
 *    OMX_StsACAAC_TnsLenErr - *pTnsRegionLen exceeds [0, numSwb] 
 *    OMX_StsACAAC_TnsOrderErr - for short window sequence, *pTnsFiltOrder 
 *              exceeds [0, 7]; For long window sequence, *pTnsFiltOrder 
 *              exceeds [0, 12] 
 *    OMX_StsACAAC_TnsCoefResErr - pTnsFiltCoefRes[w] exceeds [3, 4], w=0 to 
 *              numWin-1 
 *    OMX_StsACAAC_TnsCoefErr - *pTnsFiltCoef exceeds [-8, 7] 
 *    OMX_StsACAAC_TnsDirectErr - *pTnsDirection exceeds [0, 1] 
 *    OMX_StsACAAC_MaxSfbErr - invalid maxSfb value in relation to numSwb 
 *
 */
OMXResult omxACAAC_DecodeTNS_S32_I (
    OMX_S32 *pSrcDstSpectralCoefs,
    const OMX_INT *pTnsNumFilt,
    const OMX_INT *pTnsRegionLen,
    const OMX_INT *pTnsFiltOrder,
    const OMX_INT *pTnsFiltCoefRes,
    const OMX_S8 *pTnsFiltCoef,
    const OMX_INT *pTnsDirection,
    OMX_INT maxSfb,
    OMX_INT profile,
    OMX_INT samplingRateIndex,
    OMX_INT winLen
);



/**
 * Function:  omxACAAC_DeinterleaveSpectrum_S32   (3.2.3.5.1)
 *
 * Description:
 * Deinterleaves spectral coefficients for short block.  Sets coefficients 
 * above maxSfb equal to zero. 
 *
 * Reference: [ISO14496-3], sub-clause 4.5.2.3.5 and Figures 4.22-4.24. 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to source coefficients buffer. The coefficients are 
 *            interleaved by scalefactor window bands in each group. Buffer 
 *            length is 1024. pSrc must be 8-byte aligned. 
 *   numWinGrp - group number 
 *   pWinGrpLen - pointer to the number of windows in each group. Buffer 
 *            length is 8 
 *   maxSfb -Max scalefactor bands number for the current block 
 *   samplingRateIndex - sampling rate index. Valid in [0, 11] 
 *   winLen - the data number in one window 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to the output of coefficients. Data sequence is ordered 
 *            in pDst[w*128+sfb*sfbWidth[sfb]+i]. Where w is window index, sfb 
 *            is scalefactor band index, sfbWidth is the scalefactor band 
 *            width table, i is the index within scalefactor band. Buffer 
 *            length is 1024.  The pDst pointer must be aligned on an 8-byte 
 *            boundary. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    at least one of the following pointers is NULL: 
 *            - pSrc, 
 *            - pDst,
 *            - pWinGrpLen. 
 *    -    either pSrc or pDst are not 8-byte aligned 
 *    -    numWinGrp exceeds [1, 8] 
 *    -    maxSfb exceeds [0, 51] 
 *    -    samplingRateIndex exceeds [0, 11] 
 *    -    winLen is not 128 
 *    OMX_StsACAAC_MaxSfbErr - invalid maxSfb value in relation to numSwb 
 *
 */
OMXResult omxACAAC_DeinterleaveSpectrum_S32 (
    const OMX_S32 *pSrc,
    OMX_S32 *pDst,
    OMX_INT numWinGrp,
    const OMX_INT *pWinGrpLen,
    OMX_INT maxSfb,
    OMX_INT samplingRateIndex,
    OMX_INT winLen
);



/**
 * Function:  omxACAAC_MDCTInv_S32_S16   (3.2.3.5.2)
 *
 * Description:
 * This function computes an inverse MDCT to generate 1024 reconstructed 
 * 16-bit signed little-endian PCM samples as output for each channel. In 
 * order to adapt the time/frequency resolution of the filterbank to the 
 * characteristics of the input signal, a block switching tool is also 
 * adopted. For each channel, 1024 time-frequency domain samples are 
 * transformed into the time domain via the IMDCT. After applying the 
 * windowing operation, the first half of the windowed sequence is added to 
 * the second half of the previous block windowed sequence to reconstruct 1024 
 * output samples for each channel. Output can be interleaved according to 
 * pcmMode. 
 * If pcmMode equals 2, output is in the sequence pDstPcmAudioOut[2*i], 
 * i=0 to 1023, i.e., 1024 output samples are stored in the sequence: 
 *
 *    pDstPcmAudioOut[0],  pDstPcmAudioOut[2], 
 *    pDstPcmAudioOut[4],  ...,  pDstPcmAudioOut[2046]. 
 *
 * If pcmMode equals 1, output is in the sequence:
 * 
 *    pDstPcmAudioOut[i], i=0 to 1023. 
 *
 * Users must also preallocate an input-output buffer pointed by 
 * pSrcDstOverlapAddBuf for overlap-add operation. Reset this buffer to 
 * zero before first call, then use the output of the current call as the 
 * input of the next call for the same channel. 
 *
 * Reference: [ISO14496-3], sub-clause 4.6.11 
 *
 * Input Arguments:
 *   
 *   pSrcSpectralCoefs - pointer to the input time-frequency domain samples 
 *            in Q28.3 format. There are 1024 elements in the buffer pointed 
 *            by pSrcSpectralCoefs. 
 *   pSrcDstOverlapAddBuf - pointer to the overlap-add buffer which contains 
 *            the second half of the previous block windowed sequence in 
 *            Q28.3. There are 1024 elements in this buffer. 
 *   winSequence - analysis window sequence specifier for the current block; 
 *            the following values are allowed: 
 *            0=only_long_sequence/long_window, 
 *            1=long_start_sequence/long_start_window, 
 *            2=eight_short_sequence/short window, 
 *            3=long_stop_sequence/long_stop_window.  The function will return 
 *            an error if winSequence<0 or winSequence>3. 
 *   winShape - analysis window shape specifier for the current block.  The 
 *            following values are allowed: 0=sine window, 1=Kaiser-Bessel 
 *            derived (KBD) window.  The function will return an error if this 
 *            parameter is not equal to either 0 or 1. 
 *   prevWinShape - analysis window shape specifier for the previous block.  
 *            The following values are allowed: 0=sine window, 1=Kaiser-Bessel 
 *            derived (KBD) window.  The function will return an error if this 
 *            parameter is not equal to either 0 or 1. 
 *   pcmMode - flag that indicates whether the PCM audio output is 
 *            interleaved (LRLRLR ) or not: 1 = not interleaved 2 = 
 *            interleaved 
 *
 * Output Arguments:
 *   
 *   pDstPcmAudioOut - Pointer to the output 1024 reconstructed 16-bit signed 
 *            little-endian PCM samples in Q15.0, interleaved if needed. 
 *   pSrcDstOverlapAddBuf - pointer to the overlap-add buffer which contains 
 *            the second half of the current block windowed sequence in Q28.3. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    at least one of the pointers is NULL: 
 *           - pSrcSpectralCoefs, 
 *           - pSrcDstOverlapAddBuf, or 
 *           - pDstPcmAudioOut 
 *    -    winSequence < 0, or winSequence > 3 
 *    -    winShape < 0, or winShape > 1 
 *    -    prevWinShape < 0, or prevWinShape > 1 
 *    -    pcmMode < 1, or pcmMode > 2 
 *
 */
OMXResult omxACAAC_MDCTInv_S32_S16 (
    const OMX_S32 *pSrcSpectralCoefs,
    OMX_S16 *pDstPcmAudioOut,
    OMX_S32 *pSrcDstOverlapAddBuf,
    OMX_INT winSequence,
    OMX_INT winShape,
    OMX_INT prevWinShape,
    OMX_INT pcmMode
);



/**
 * Function:  omxACAAC_DecodeMsPNS_S32_I   (3.2.3.6.1)
 *
 * Description:
 * Performs perceptual noise substitution for one channel across all window 
 * groups and scalefactor bands.  PNS is activated for SFBs labeled in the 
 * pSfbCb vector to be of type NOISE_HCB. For PNS scalefactor bands, spectral 
 * coefficients are derived from random vectors rather than from decoded 
 * Huffman symbols. 
 *
 * Reference: [ISO14496-3], sub-clause 4.6.13 
 *
 * Input Arguments:
 *   
 *   pSrcDstSpec - pointer to the spectral coefficient vector to which PNS 
 *            should be applied 
 *   pSrcDstLtpFlag - pointer to LTP used flag 
 *   pSfbCb - pointer to scalefactor codebook; PNS is applied to SFBs tagged 
 *            with NOISE_HCB 
 *   pScaleFactor - pointer to the scalefactor value 
 *   maxSfb - number of scale factor bands used 
 *   numWinGrp - number of window group 
 *   pWinGrpLen - pointer to the length of every window group 
 *   samplingFreqIndex - sampling frequency index 
 *   winLen - window length, 1024 for long, 128 for short 
 *   pRandomSeed - random seed for PNS 
 *   channel - index of current channel, 0:left, 1:right 
 *   pMsUsed - pointer to MS used buffer from the CPE structure 
 *   pNoiseState - pointer to random noise generator seed history buffer, of 
 *            dimension [OMX_AAC_GROUP_NUM_MAX][OMX_AAC_SF_MAX].  If channel==0, 
 *            this buffer is used only as an output and the contents upon 
 *            input are ignored.  If  channel==1 the entries in this buffer 
 *            are used to seed the PNS random number generator for each 
 *            scalefactor band in which pMsUsed==1 in order to guarantee L-R 
 *            correlation in those particular SFBs. Correlation is guaranteed 
 *            as long as the seed entries were previously stored into this 
 *            buffer during a prior call to the function with the input 
 *            parameter channel==0. 
 *
 * Output Arguments:
 *   
 *   pSrcDstSpec - pointer to updated spectral coefficient vector after 
 *            completion of PNS 
 *   pSrcDstLtpFlag - pointer to the LTP used flag 
 *   pRandomSeed - updated PNS random seed 
 *   pNoiseState - random seed buffer, of dimension [OMX_AAC_GROUP_NUM_MAX] 
 *            [OMX_AAC_SF_MAX].  Two possible return conditions are possible:  
 *            If channel==0, this buffer returns the complete set of left 
 *            channel random seeds used at the start of PNS synthesis for 
 *            every scalefactor band in every group for which pSfbCb == 
 *            NOISE_HCB.  If channel==1 the buffer is used as an input only 
 *            and the contents are unchanged from input to output. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_StsACAAC_MaxSfbErr - invalid maxSfb value in relation to numSwb 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    at least one of the pointers is NULL: 
 *          - pSrcDstSpec, 
 *          - pSfbCb, 
 *          - pScaleFactor, 
 *          - pWinGrpLenor 
 *          - pSrcDstLtpFlag 
 *    -    numWinGrp exceeds [1, 8] 
 *    -    samplingFreqIndex exceeds [0,12] 
 *    -    winLen is neither 128 nor 1024 
 *    -    maxSfb not in [0,51] 
 *
 */
OMXResult omxACAAC_DecodeMsPNS_S32_I (
    OMX_S32 *pSrcDstSpec,
    OMX_INT *pSrcDstLtpFlag,
    OMX_U8 *pSfbCb,
    OMX_S16 *pScaleFactor,
    OMX_INT maxSfb,
    OMX_INT numWinGrp,
    OMX_INT *pWinGrpLen,
    OMX_INT samplingFreqIndex,
    OMX_INT winLen,
    OMX_INT *pRandomSeed,
    OMX_INT channel,
    OMX_U8 *pMsUsed,
    OMX_INT *pNoiseState
);



/**
 * Function:  omxACAAC_LongTermReconstruct_S32_I   (3.2.3.7.1)
 *
 * Description:
 * Reconstruction portion of the LTP loop; adds the vector of decoded 
 * spectral coefficients and the corresponding spectral-domain LTP output 
 * vector to obtain a vector of reconstructed spectral samples. 
 *
 * Reference: [ISO14496-3], sub-clause 4.6.7 
 *
 * Input Arguments:
 *   
 *   pSrcDstSpec - pointer to decoded spectral coefficients; coefficients are 
 *            represented using Q28.3 
 *   pSrcEstSpec - pointer to the spectral-domain LTP output vector; 
 *            coefficients are represented using Q28.3 
 *   samplingFreqIndex - sampling frequency index 
 *   pLtpFlag - pointer to the vector of scalefactor band LTP indicator flags 
 *
 * Output Arguments:
 *   pSrcDstSpec - pointer to reconstructed spectral coefficient vector; 
 *            coefficients are represented using Q28.3 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    one or more of the following pointers is NULL: 
 *            - pSrcDstSpec, 
 *            - pSrcEstSpec, or 
 *            - pLtpFlag 
 *    -    samplingFreqIndex is outside the range [0,12] 
 *
 */
OMXResult omxACAAC_LongTermReconstruct_S32_I (
    OMX_S32 *pSrcDstSpec,
    OMX_S32 *pSrcEstSpec,
    OMX_INT *pLtpFlag,
    OMX_INT samplingFreqIndex
);



/**
 * Function:  omxACAAC_MDCTFwd_S32   (3.2.3.7.2)
 *
 * Description:
 * Forward MDCT portion of the LTP loop; used only for audio objects of type 
 * LTP. 
 *
 * Reference: [ISO14496-3], sub-clause 4.6.7.3 
 *
 * Input Arguments:
 *   
 *   pSrc - pointer to the time-domain input sequence; samples are 
 *            represented using Q28.3 
 *   winSequence - window sequence specifier for the current block; the 
 *            following values are allowed:  0=only_long_sequence/long_window, 
 *            1=long_start_sequence/long_start_window, 
 *            3=long_stop_sequence/long_stop_window.  The function will return 
 *            an error if winSequence==2,as short window sequences are not 
 *            allowed in the LTP reconstruction loop for AAC LTP audio 
 *            objects. 
 *   winShape - window shape specifier for the current block.  The following 
 *            values are allowed:  0=sine window, 1=Kaiser-Bessel derived 
 *            (KBD) window.  The function will return an error if this 
 *            parameter is not equal to either 0 or 1. 
 *   preWinShape - analysis window shape specifier for the previous block. 
 *            The following values are allowed: 0=sine window, 1=Kaiser-Bessel 
 *            derived (KBD) window.  The function will return an error if this 
 *            parameter is not equal to either 0 or 1. 
 *   pWindowedBuf - work buffer; minimum length 2048 elements 
 *
 * Output Arguments:
 *   
 *   pDst - pointer to MDCT output sequence; coefficients are represented 
 *            using Q28.3 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    one or more of the following pointers is NULL: pSrc, pDst, or 
 *              pWindowedBuf 
 *    -    winShape is outside the range [0,1] 
 *    -    preWinShape is outside the range [0,1] 
 *    -    winSequence== 2 (eight_short_sequence/short_window) 
 *
 */
OMXResult omxACAAC_MDCTFwd_S32 (
    OMX_S32 *pSrc,
    OMX_S32 *pDst,
    OMX_INT winSequence,
    OMX_INT winShape,
    OMX_INT preWinShape,
    OMX_S32 *pWindowedBuf
);



/**
 * Function:  omxACAAC_EncodeTNS_S32_I   (3.2.3.7.3)
 *
 * Description:
 * This function applies a TNS analysis (encoding) filter to spectral 
 * coefficients in the LTP feedback loop for one channel. 
 *
 * Reference: [ISO14496-3], sub-clause 4.6.9 
 *
 * Input Arguments:
 *   
 *   pSrcDstSpectralCoefs - pointer to the unprocessed spectral coefficient 
 *            vector for one channel; coefficients are represented using Q28.3 
 *   pTnsNumFilt - pointer to a table containing the number of TNS filters 
 *            that are applied on each window of the current frame for the 
 *            current channel. The table elements are indexed as follows:   
 *                             pTnsNumFilt[w] 
 *            w=0 to numWin-1; depending upon the current window sequence, 
 *            this vector may contain up to 8 elements 
 *   pTnsRegionLen - pointer to a table containing TNS region lengths (in 
 *            scalefactor band units) for all regions and windows on the 
 *            current frame for the current channel; the table entry 
 *            pTnsRegionLen[i] specifies the region length for k-th filter on 
 *            the w-th window. The table index, i, is computed as follows: 
 *
 *                       i = SUM[j=0; w-1] (pnsNumFilt[j] + k)
 *                    
 *            where 0 <= w <= numWin-1, and 0 <= k <= pTnsNumFilt[w]-1. 
 *
 *   pTnsFiltOrder - pointer to a table containing TNS filter orders for all 
 *            regions and windows on the current frame for the current 
 *            channel; the table entry pTnsFiltOrder[i] specifies the TNS 
 *            filter order for the k-th filter on the w-th window. The table 
 *            index, i, is computed as follows: 
 *
 *                       i = SUM[j=0; w-1] (pnsNumFilt[j] + k)
 *                    
 *               where 0 <= w <= numWin-1, and 0 <= k <= pTnsNumFilt[w]-1. 
 *
 *   pTnsFiltCoefRes - pointer to a table of TNS filter coefficient 
 *            resolution indicators for each window on the current frame for 
 *            the current channel. Resolutions for filters on the w-th window 
 *            are specified in table entry pTnsFiltCoefRes[w], and w=0 to 
 *            numWin-1. 
 *   pTnsFiltCoef - pointer to a table containing the complete set of TNS 
 *            filter coefficients for all windows and regions on the current 
 *            frame for the current channel.  Filter coefficients are stored 
 *            contiguously in filter-major order, i.e., the table is organized 
 *            such that the filter coefficients for the k th filter of the 
 *            w-th window are indexed using:
 *
 *                       pTnsFiltCoef[w][k][i], 
 *
 *               where 0 <= i <= pTnsFiltOrder[j]-1, 
 *                     0 <= k <= pTnsNumFilt[w]-1, 
 *                     0 <= w <= numWin-1, 
 *                     and the filter order index j is computed as shown above. 
 *
 *   pTnsDirection - pointer to a table of tokens that indicate the direction 
 *            of TNS filtering for all regions and windows on the current 
 *            frame, with 0 indicating upward and 1 indicating downward; in 
 *            particular the table entry pTnsDirection[i] specifies direction 
 *            for k-th filter on the w-th window, and the table index, i, is 
 *            computed as follows: 
 *
 *                       i = SUM[j=0; w-1] (pnsNumFilt[j] + k)
 *
 *                where 0 <= w <= numWin-1, and 0 <= k <= pTnsNumFilt[w]-1. 
 *
 *   maxSfb - number of scalefactor bands 
 *   profile - audio profile 
 *   samplingRateIndex - sampling rate index 
 *
 * Output Arguments:
 *   
 *   pSrcDstSpectralCoefs - pointer to the TNS-encoded spectral coefficient 
 *            vector; coefficients are represented using Q28.3 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_StsACAAC_MaxSfbErr - invalid maxSfb value in relation to numSwb 
 *    OMX_Sts_BadArgErr - bad arguments 
 *     -   at least one of the pointers is NULL: 
 *          - pSrcDstSpectralCoefs, 
 *          - pTnsNumFilt, 
 *          - pTnsRegionLen, 
 *          - pTnsFiltOrder, 
 *          - pTnsFiltCoefRes, 
 *          - pTnsFiltCoefor 
 *          - pTnsDirection 
 *     -  samplingRateIndex exceeds [0,12] 
 *     -  maxSfb not in [0,51] 
 *
 */
OMXResult omxACAAC_EncodeTNS_S32_I (
    OMX_S32 *pSrcDstSpectralCoefs,
    const OMX_INT *pTnsNumFilt,
    const OMX_INT *pTnsRegionLen,
    const OMX_INT *pTnsFiltOrder,
    const OMX_INT *pTnsFiltCoefRes,
    const OMX_S8 *pTnsFiltCoef,
    const OMX_INT *pTnsDirection,
    OMX_INT maxSfb,
    OMX_INT profile,
    OMX_INT samplingRateIndex
);



/**
 * Function:  omxACAAC_LongTermPredict_S32   (3.2.3.7.4)
 *
 * Description:
 * LTP analysis portion of the LTP loop. 
 *
 * Reference: [ISO14496-3], sub-clause 4.6.7 
 *
 * Input Arguments:
 *   
 *   pSrcTimeSignal - pointer to the time-domain sequence to be predicted; 
 *            samples are represented using Q28.3 
 *   pAACLtpInfo - pointer to the LTP configuration information 
 *
 * Output Arguments:
 *   
 *   pDstEstTimeSignal - pointer to the LTP output sequence; samples are 
 *            represented using Q28.3 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments: 
 *    -   one or more of the following pointers is NULL: 
 *              - pSrcDstTime, 
 *              - pAACLtpInfo, or 
 *              - pDstEstTimeSignal 
 *    -    the value of the parameter pAACLtpInfo->ltpCoef does not lie in 
 *              the range [9352, 22438] 
 *
 */
OMXResult omxACAAC_LongTermPredict_S32 (
    OMX_S32 *pSrcTimeSignal,
    OMX_S32 *pDstEstTimeSignal,
    OMXAACLtpInfo *pAACLtpInfo
);



/**
 * Function:  omxACAAC_NoiselessDecode   (3.2.3.8.1)
 *
 * Description:
 * Noiseless decoder for a single channel of AAC LC and LTP audio objects. 
 * Extracts side information, scalefactor information, quantized spectral 
 * coefficients, TNS parameters, and LTP parameters from the input stream for 
 * one channel and places the contents into the arrays referenced by the 
 * parameters pChanInfo, pDstScalefactor, pDstQuantizedSpectralCoef, 
 * pDstTnsFiltCoef, and pLtpInfo, respectively.  Individual output structure 
 * member update dependencies on elementary stream properties are specified 
 * below under  Output Arguments  for each parameter. 
 *
 * Reference: [ISO14496-3], sub-clause 4.6.3 
 *
 * Input Arguments:
 *   
 *   ppBitStream - double pointer to current byte in the input bitstream 
 *   pOffset - pointer to the offset indicating the next available bit in the 
 *            current byte of the input bitstream; valid in the rage [0,7]. 
 *   pChanInfo - pointer to the channel information structure; the structure 
 *            member samplingRateIndex must contain valid information prior to 
 *            calling this function.  The remaining structure members are 
 *            updated upon return as described under  Output Arguments.  
 *   commonWin - commonWin==1 indicates that the channel pair uses the same 
 *            individual channel stream information (ICS); commonWin==0 
 *            indicates that ICS is not shared across a channel pair. 
 *   audioObjectType - audio object type indicator: 2=LC, 4=LTP 
 *
 * Output Arguments:
 *   
 *   ppBitStream - double pointer to the updated stream pointer; references 
 *            the current byte in the input bitstream after Huffman decoding 
 *            has been completed 
 *   pOffset - pointer to the updated bit index indicating the next available 
 *            bit in the input stream following after Huffman decoding has 
 *            been completed 
 *   pChanInfo - pointer to the updated channel information structure. 
 *            NoiselessDecode updates all members of this structure, with the 
 *            following exceptions:  i) the following members are never 
 *            updated by this function: tag, id, predSfbMax, preWinShape, and 
 *            pChanPairElt; ii) if commonWin==1 then the contents of ICS 
 *            structure *(pChanInfo->pIcsInfo) are not modified (for the 
 *            common window case, refer to function 
 *            omxACAAC_DecodeChanPairElt); iii) if commonWin==0 then all 
 *            members of the ICS structure *(pChanInfo->pIcsInfo) are modified 
 *            unconditionally except as shown in Table 3-11, and only the 
 *            first pIcsInfo->numWinGrp elements in pIcsInfo-> pWinGrpLen are 
 *            updated; iv) only elements 0, 1, 2,...pIcsInfo->maxSfb-1 of 
 *            arrays pSectCb[.] and pSectEnd[.] are updated; v) only elements 
 *            0, 1, 2,..., pIcsInfo >numWinGrp of arrays pMaxSect[.], 
 *            pTnsNumFilt[.], and pTnsFiltCoefRes[.] are updated; vi) only 
 *            elements 0, 1, 2,..., sum(pTnsNumFilt[i]) for i = 0, 1,  , 
 *            (pIcsInfo >numWinGrp)-1 are updated for arrays pTnsRegionLen[.], 
 *            pTnsFilterOrder[.], and pTnsDirection[.]. The updated TNS 
 *            parameters returned in the TNS parameter arrays pTnsNumFilt, 
 *            pTnsFiltCoefRes, pTnsRegionLen, pTnsFiltOrder, and pTnsDirection 
 *            are organized as described in the corresponding input parameter 
 *            descriptions given in section 3.2.7.2.3 (EncodeTNS_S32_I). 
 *   pDstScalefactor - pointer to the updated scalefactor table; the buffer 
 *            must have sufficient space to contain up to 120 scalefactor 
 *            elements. 
 *   pDstQuantizedSpectralCoef - pointer to the 1024-element array containing 
 *            the decoded, quantized spectral coefficients, all of which are 
 *            integer values represented using Q0, i.e., no scaling. 
 *   pDstSfbCb - pointer to the updated table of scalefactor band codebook 
 *            indices; the buffer must have sufficient space to contain up to 
 *            120 SFB codebook indices. 
 *   pDstTnsFiltCoef - pointer to the updated table containing the complete 
 *            set of TNS filter coefficients for all windows and regions on 
 *            the current channel.  Filter coefficients are stored 
 *            contiguously in filter-major order, i.e., the table is organized 
 *            such that the filter coefficients for the k-th filter of the 
 *            w-th window are indexed using pTnsFiltCoef[w][k][i], where 0 <= 
 *            i <= pTnsFiltOrder[j]-1, 0 <= k <= pTnsNumFilt[w]-1, 0 <= w <= 
 *            numWin-1, and the filter order index, j, is computed as 
 *            described in section 3.2.7.2.3 (EncodeTNS_S32_I) under the 
 *            pTnsFiltOrder input parameter description. 
 *   pLtpInfo - pointer to the LTP information structure associated with the 
 *            current channel; updated only if (pChanPairElt->CommonWin == 0) 
 *            && (audioObjectType==4) and the elementary stream bit field 
 *            predictor_data_present==1. Otherwise, if 
 *            (pChanPairElt >CommonWin == 1) || (audioObjectType!=4) || 
 *            predictor_data_present==0 then the function 
 *            omxACAAC_NoiselessDecode will not update the contents of the 
 *            structure *pLtpInfo. The LTP information structure will be 
 *            updated by the function omxACAAC_DecodeChanPairElt if 
 *            (pChanPairElt->CommonWin == 1) && (audioObjectType==4). 
 *            Reference:  [ISO14496-3], sub-clause 4.4.2.1 and Tables 4.5 4.6. 
 *
 * Return Value:
 *    
 *    OMX_Sts_NoErr - no error 
 *    OMX_Sts_BadArgErr - bad arguments 
 *    -    at least one of the pointers: ppBitStream, pOffset, *ppBitStream, 
 *              pDstScaleFactor, pDstTnsFiltCoef, pDstQuantizedSpectralCoef, 
 *              pChanInfo or pDstSfbCb is NULL. 
 *    -    *pOffset exceeds [0,7] 
 *    -    commonWin exceeds [0,1] 
 *    -    audioObjectType is not equal to either 2 or 4 
 *    -    commonWin==1 and pChanInfo->pIcsInfo->winSequence exceeds [0,3] 
 *    OMX_Sts_InvalidBitstreamValErr - invalid bitstream parameter value 
 *              detected 
 *    -    commonWin==0 and the decoded value of pChanInfo->pIcsInfo->maxSfb 
 *              is out of range, i.e., exceeds [0,51] 
 *    OMX_StsACAAC_PlsDataErr - pulse data error; returned if one or more of 
 *              the following conditions is true: 
 *           i) pulse data is present during a short window sequence, i.e., 
 *              pChanInfo >pIcsInfo->winSequence==EIGHT_SHORT_SEQUENCE && 
 *              pChanInfo->pulsePres==1; 
 *          ii) the start scalefactor band for pulse data (pulse_start_sfb)
 *              is out of range, i.e., 
 *              (pulse_start_sfb>= pChanInfo->numSwb) || (pulse_start_sfb >= 51);
 *         iii) pulse data position offset (pulse_offset[i]), is out 
 *              of range, i.e., pulse_offset[i]>= pChanInfo->winLen. 
 *              Reference: [ISO14496-3], sub-clause 4.6.3. 
 *    OMX_StsACAAC_GainCtrErr - pChanInfo->gainContrDataPres==1. 
 *
 */
OMXResult omxACAAC_NoiselessDecode (
    const OMX_U8 **ppBitStream,
    OMX_INT *pOffset,
    OMX_S16 *pDstScalefactor,
    OMX_S32 *pDstQuantizedSpectralCoef,
    OMX_U8 *pDstSfbCb,
    OMX_S8 *pDstTnsFiltCoef,
    OMXAACChanInfo *pChanInfo,
    OMX_INT commonWin,
    OMX_INT audioObjectType,
    OMXAACLtpInfo *pLtpInfo
);



#ifdef __cplusplus
}
#endif

#endif /** end of #define _OMXAC_H_ */

/** EOF */

