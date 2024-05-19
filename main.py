import json


def get_json_config(path='config.json'):
	with open(path, 'r') as config_file:
		config = json.load(config_file)
	in_path = config['input']
	out_path = config['output']
	wk_part = config['percent_of_normal_worktime']
	return in_path, out_path, wk_part


def get_report_data(path):
	with open(path, 'r', encoding='utf-8') as report:
		work_time = int(report.readline())
		data = {}
		line = report.readline()
		while line:
			line = line.split()
			if line[0] not in data:
				data[line[0]] = [line[1] + ' ' + line[2][0] + '.' + line[3][0] + '.', 0]
			data[line[0]][1] += float(line[-1])
			line = report.readline()
	disbalance = []
	for id in data:
		diff = abs(data[id][-1] - work_time)
		if diff > work_time * work_part / 100:
			hours = int(diff) if int(diff) == diff else diff
			disbalance.append([work_time < data[id][-1]] + data[id][:1] + [str(hours)])
	disbalance.sort()
	return disbalance


def write_disbalance(disbalance, path):
	with open(path, 'w', encoding='utf-8') as result_file:
		for employee in disbalance:
			sign = '+' if employee[0] else '-'
			result_file.write(employee[1] + ' ' + sign + employee[-1])
			if employee != disbalance[-1]:
				result_file.write('\n')


if __name__ == '__main__':
	input_path, output_path, work_part = get_json_config()

	disbal = get_report_data(input_path)
	write_disbalance(disbal,output_path)
	print('!!!Программа завершила работу!!!')