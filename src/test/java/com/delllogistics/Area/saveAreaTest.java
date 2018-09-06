package com.delllogistics.Area;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.delllogistics.entity.sys.SysArea;
import com.delllogistics.repository.sys.SysAreaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class saveAreaTest {
	@Autowired
	protected WebApplicationContext wac;

	@Autowired
	private SysAreaRepository sysAreaRepository;





	@Test
	public void saveArea() throws Exception {


		Integer level1Id = 0;
		File jsonfile = ResourceUtils.getFile("classpath:areaJson/area"+level1Id+".json");
		String fileName = jsonfile.getAbsolutePath();
		BufferedReader br = new BufferedReader(new FileReader(fileName));// 读取原始json文件
		String s, ws = "";
		while ((s = br.readLine()) != null) try {
			ws += s;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		br.close();
		SysArea lev1Parent = sysAreaRepository.findOne(Long.parseLong(level1Id.toString()) );//北京等主键


		JSONObject jsonObject = JSON.parseObject(ws);
		for (String lev : jsonObject.keySet()) {//System.out.println("map.get(" + lev + ") = " + jsonObject.get(lev).toString());
			String strClassSimpleNameLev1 = jsonObject.get(lev).getClass().getSimpleName();//JSONArray
			if (strClassSimpleNameLev1.equals("JSONArray")) {
				JSONArray levArray = JSON.parseArray(jsonObject.get(lev).toString());
				SysArea lev2Parent = new SysArea();
				for (Object obj : levArray) {
					String strClassSimpleNameLev2 = obj.getClass().getSimpleName();//JSONArray
					if (strClassSimpleNameLev2.equals("String")) {
						String lev2 = obj.toString();
						SysArea sysArea = new SysArea();
						sysArea.setCode("");
						sysArea.setParent(lev1Parent);
						sysArea.setLevel(2);
						sysArea.setSort(Integer.parseInt(lev));
						sysArea.setName(lev2.toString());
						lev2Parent = sysAreaRepository.save(sysArea);
						System.out.println("=========" + lev2.toString() + "(" + lev + ")(" + lev2Parent.getId() + ")=========");
					}
					if (strClassSimpleNameLev2.equals("JSONObject")) {

						JSONObject lev3Object = JSON.parseObject(obj.toString());
						for (String lev3 : lev3Object.keySet()) {
							String strClassSimpleNameLev3 = lev3Object.get(lev3).getClass().getSimpleName();//JSONArray
							SysArea lev3Parent = new SysArea();
							if (strClassSimpleNameLev3.equals("JSONArray")) {
								JSONArray lev3Array = JSON.parseArray(lev3Object.get(lev3).toString());
								for (Object obj3 : lev3Array) {
									String strClassSimpleNameLev4 = obj3.getClass().getSimpleName();//JSONArray
									if (strClassSimpleNameLev4.equals("String")) {
										SysArea sysArea = new SysArea();
										sysArea.setCode("");
										sysArea.setParent(lev2Parent);
										sysArea.setLevel(3);
										sysArea.setSort(Integer.parseInt(lev3));
										sysArea.setName(obj3.toString());
										lev3Parent = sysAreaRepository.save(sysArea);
										System.out.println("======" + obj3.toString() + "(" + lev3 + ")(" + lev3Parent.getId() + ")=========");
									}
									if (strClassSimpleNameLev4.equals("JSONObject")) {
										JSONObject lev4Object = JSON.parseObject(obj3.toString());
										for (String lev4 : lev4Object.keySet()) {
											SysArea sysArea = new SysArea();
											sysArea.setCode("");
											sysArea.setParent(lev3Parent);
											sysArea.setLevel(4);
											sysArea.setSort(Integer.parseInt(lev4));
											sysArea.setName(lev4Object.get(lev4).toString());
											SysArea lev4Parent = sysAreaRepository.save(sysArea);

											String strClassSimpleNameLev5 = lev4Object.get(lev4).getClass().getSimpleName();//JSONArray
											System.out.println("======（3）" + lev4Object.get(lev4).toString() + "(" + lev3 + ")(" + lev4Parent.getId() + ")=========");

										}
									}

								}
							}
							if (strClassSimpleNameLev3.equals("String")) {
								SysArea sysArea = new SysArea();
								sysArea.setCode("");
								sysArea.setParent(lev2Parent);
								sysArea.setLevel(3);
								sysArea.setSort(Integer.parseInt(lev3));
								sysArea.setName(lev3Object.get(lev3).toString());
								lev3Parent = sysAreaRepository.save(sysArea);
								System.out.println("======（3）" + lev3Object.get(lev3).toString() + "(" + lev3 + ")(" + lev3Parent.getId() + ")=========");
							}
						}
					}


				}
			}
			if (strClassSimpleNameLev1.equals("String")) {
				SysArea sysArea = new SysArea();
				sysArea.setCode("");
				sysArea.setParent(lev1Parent);
				sysArea.setLevel(2);
				sysArea.setSort(Integer.parseInt(lev));
				sysArea.setName(jsonObject.get(lev).toString());
				SysArea lev2Parent = sysAreaRepository.save(sysArea);
				System.out.println("======（3）" + jsonObject.get(lev).toString() + "(" + lev + ")(" + lev2Parent.getId() + ")=========");
			}
		}


	}
	@Test
	public void contextLoads() {




	}

}
