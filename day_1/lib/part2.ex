defmodule Part2 do
  def to_int(str) do
    IO.puts("Converting #{str} to integer")

    numbers = ["zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"]

    String.replace(str, numbers, fn x ->
      Enum.find_index(numbers, &(&1 == x))
      |> Integer.to_string()
    end)
  end

  def sum_values(file) do
    digits = "zero|one|two|three|four|five|six|seven|eight|nine|[0-9]"
    re = ~r/(^#{digits})|(#{digits}$)/

    File.read!(file)
    |> String.split("\n")
    |> Enum.map(fn line ->
      Regex.split(
        re,
        line,
        include_captures: true
      )
      # remove empty strings and strings that don't contain digits
      |> Enum.reject(&(&1 == "" || Regex.scan(~r/(#{digits})/, &1) == []))
    end)
    # get first and last elements of each list
    |> Enum.map(fn elem ->
      case Enum.count(elem) do
        1 -> [elem]
        _ -> [Enum.at(elem, 0), Enum.at(elem, -1)]
      end
    end)
    |> Enum.map(fn elem ->
      Enum.map(elem, fn inner ->
        case Integer.parse(inner) do
          {int, _} -> [int]
          :error -> [Part2.to_int(inner)]
        end
      end)
      |> Enum.join()
      |> String.to_integer()
    end)
    |> Enum.sum()
  end
end
