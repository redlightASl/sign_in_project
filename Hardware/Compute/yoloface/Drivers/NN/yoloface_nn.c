#include <stdio.h>
#include <math.h>
#include "main.h"
#include "yoloface.h"
#include "yoloface_data.h"
#include "yoloface_nn.h"

///* Global handle to reference an instantiated C-model */
//static ai_handle network = AI_HANDLE_NULL;
//static ai_network_report report;

///* Global c-array to handle the activations buffer */
//AI_ALIGNED(32)
//static ai_u8 activations[AI_YOLOFACE_DATA_ACTIVATIONS_SIZE];

///* AI buffer IO handlers */
//ai_u8 in_data[AI_YOLOFACE_IN_1_SIZE_BYTES];
//ai_u8 out_data[AI_YOLOFACE_OUT_1_SIZE_BYTES];
//ai_buffer *ai_input;
//ai_buffer *ai_output;


///* 
// * Bootstrap code
// */
//int aiInit(void) 
//{
//	ai_error err;
//	
//	const ai_handle acts[] = { activations };
//    err = ai_yoloface_create_and_init(&network, acts, NULL);
//    if (err.type != AI_ERROR_NONE) 
//	{
//        printf("ai init_and_create error\n");
//        return -1;
//    }
//    /** @brief {optional} for debug/log purpose */
//    if (ai_yoloface_get_report(network, &report) != true) 
//	{
//        printf("ai get report error\n");
//        return -1;
//    }
//    printf("Model name      : %s\n", report.model_name);
//    printf("Model signature : %s\n", report.model_signature);

//    ai_input = &report.inputs[0];
//    ai_output = &report.outputs[0];
//    printf("input[0] : (%d, %d, %d)\n", AI_BUFFER_SHAPE_ELEM(ai_input, AI_SHAPE_HEIGHT),
//                                        AI_BUFFER_SHAPE_ELEM(ai_input, AI_SHAPE_WIDTH),
//                                        AI_BUFFER_SHAPE_ELEM(ai_input, AI_SHAPE_CHANNEL));
//    printf("output[0] : (%d, %d, %d)\n", AI_BUFFER_SHAPE_ELEM(ai_output, AI_SHAPE_HEIGHT),
//                                         AI_BUFFER_SHAPE_ELEM(ai_output, AI_SHAPE_WIDTH),
//                                         AI_BUFFER_SHAPE_ELEM(ai_output, AI_SHAPE_CHANNEL));
//	
//  return 0;
//}

///* 
// * Run inference code
// */
//int aiRun(const void *in_data, void *out_data)
//{
//  ai_i32 n_batch;
//  ai_error err;



//  

//	/** @brief Create the AI buffer IO handlers
//     *  @note  ai_inuput/ai_output are already initilaized after the
//     *         ai_network_get_report() call. This is just here to illustrate
//     *         the case where get_report() is not called.
//     */
//    ai_input = ai_yoloface_inputs_get(network, NULL);
//    ai_output = ai_yoloface_outputs_get(network, NULL);

//    /** @brief Set input/output buffer addresses */
//    ai_input[0].data = AI_HANDLE_PTR(in_data);
//    ai_output[0].data = AI_HANDLE_PTR(out_data);

//    /** @brief Perform the inference */
//    n_batch = ai_yoloface_run(network, &ai_input[0], &ai_output[0]);
//    if (n_batch != 1) 
//	{
//        err = ai_yoloface_get_error(network);
//        printf("ai run error %d, %d\n", err.type, err.code);
//      return -1;
//    }
//  
//	return 0;
//}

//void pre_process(void)
//{
//	for(int i = 0; i < 56; i++)
//	{
//		for(int j = 0; j < 56; j++)
//		{
//			uint16_t color = pic[j][i*FrameWidth];
//			// 这里要注意，网络的输入张量维度是BHWC，对应1*56*56*3，通道顺序是RGB
//            // 所以输入数组的存储顺序应该是先行后列，颜色是R G B
//			in_data[(j+i*56)*3] = (int8_t)((color&0xF800)>>9) - 128;
//			in_data[(j+i*56)*3+1] = (int8_t)((color&0x07E0)>>3) - 128;
//			in_data[(j+i*56)*3+2] = (int8_t)((color&0x001F)<<3) - 128;
//		}
//	}
//}

//void post_process(void)
//{

//}
















