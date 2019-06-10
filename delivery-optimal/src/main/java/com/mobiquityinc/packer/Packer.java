package com.mobiquityinc.packer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mobiquityinc.delivery_optimal.model.EnumConstants;
import com.mobiquityinc.delivery_optimal.model.EnumException;
import com.mobiquityinc.delivery_optimal.model.Pack;
import com.mobiquityinc.exception.APIException;

/**
 * 
 * This class is responsible for keep the transaction to choose he best items to delivery 
 * 
 * @author nilsonmassarenti
 *
 */
public class Packer {

	/**
	 * 
	 * This method is responsible for receive the file path to processing the packages
	 * 
	 * @param filePath
	 * @return
	 * @throws APIException
	 */
	public static String pack(String filePath) throws APIException {
		InputStream input = readFile(filePath);
		if (input == null) {
			throw new APIException(EnumException.FILE_NOT_FOUND.getMessage());
		}
		try {
			if (input.available() == 0) {
				throw new APIException(EnumException.FILE_EMPTY.getMessage());
			}
		} catch (IOException e1) {
			throw new APIException(EnumException.FILE_NOT_FOUND.getMessage(), e1);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		String line = null;
		StringBuffer sPack = new StringBuffer();
		try {
			while ((line = br.readLine()) != null) {
				String data[] = line.split(" : ");
				Double totalWeight = Double.parseDouble(data[0]);
				if (totalWeight <= EnumConstants.MAX_PACKAGE_WEIGHT.getValue()) {
					List<Pack> packs = convertLine(data[1]);
					packs = removeOutItems(packs, totalWeight);
					sPack.append(findOptimalDelivery(packs, totalWeight) + "\n");
				} else {
					sPack.append("-\n");
				}

			}
		} catch (IOException e) {
			throw new APIException(EnumException.FILE_ERROR_READ.getMessage(), e);
		}
		return sPack.toString().replaceAll("[\n\r]+$", "");
	}

	/**
	 * 
	 * This method is responsible for remove the items out of parameters and sort by cost
	 * 
	 * @param packs
	 * @param totalWeight
	 * @return
	 */
	private static List<Pack> removeOutItems(List<Pack> packs, Double totalWeight) {
		return packs = packs.stream().filter(fil -> {
			Double weight = fil.getWeight();
			Double cost = fil.getCost();
			if ((weight > totalWeight) || cost > EnumConstants.MAX_ITEM_COST.getValue()) {
				return false;
			} else {
				return true;
			}
		}).sorted(Comparator.comparing(Pack::getCost).reversed()).collect(Collectors.toList());
	}

	
	/**
	 * 
	 * This method is responsible for read the file from path
	 * 
	 * @param filePath
	 * @return
	 */
	private static InputStream readFile(String filePath) {
		File initialFile = new File(filePath);
		InputStream input = null;
		try {
			input = new FileInputStream(initialFile);
		} catch (FileNotFoundException e) {
			return null;
		}
		return input;
	}

	/**
	 * 
	 * THis method is responsible for convert theline to right register
	 * 
	 * @param line
	 * @return
	 */
	private static List<Pack> convertLine(String line) {
		List<Pack> packs = new ArrayList<Pack>();
		String data[] = line.split(" ");
		for (String info : data) {
			String infos[] = info.replace("(", "").replace(")", "").split(",");
			if (infos.length == 3) {
				Pack pack = new Pack(Integer.parseInt(infos[0]), Double.parseDouble(infos[1]),
						Double.parseDouble(infos[2].replaceAll("\\D", "")), infos[2].replaceAll("\\d", ""));
				packs.add(pack);
			}
		}
		return packs;
	}

	/**
	 * 
	 * this method is responsible form find the best and optimal packages to delivery
	 * 
	 * @param packs
	 * @param totalWeight
	 * @return
	 */
	private static String findOptimalDelivery(List<Pack> packs, Double totalWeight) {
		if (packs.size() == 0) {
			return "-";
		}
		if (packs.size() == 1) {
			return packs.get(0).getIndex().toString();
		}
		Double minWeight = 0d;
		Double auxWeight = 0d;
		Double auxCost = 0d;
		Double maxCost = 0d;
		List<Integer> seq = new ArrayList<>();
		List<Integer> auxSeq = new ArrayList<>();
		for (int i = 0; i < packs.size(); i++) {
			auxWeight = packs.get(i).getWeight();
			auxSeq.add(packs.get(i).getIndex());
			auxCost = packs.get(i).getCost();
			for (int j = i + 1; j < packs.size(); j++) {
				if ((auxWeight + packs.get(j).getWeight()) <= totalWeight) {
					auxWeight += packs.get(j).getWeight();
					auxCost += packs.get(j).getCost();
					auxSeq.add(packs.get(j).getIndex());
				}
			}
			if (auxCost >= maxCost) {
				if (minWeight == 0 || auxWeight < minWeight) {
					minWeight = auxWeight;
					seq.removeAll(seq);
					seq.addAll(auxSeq);
					maxCost = auxCost;
				}

			}
			auxSeq.removeAll(auxSeq);
		}
		return seq.stream().map(Object::toString).sorted().collect(Collectors.joining(","));
	}
}
